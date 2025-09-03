package com.wagnerdf.fancollectorsmedia.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class IgdbService {

    @Autowired
    private ApiConfigService apiConfigService;

    private static final String CLIENT_ID_KEY = "IGDB_CLIENT_ID";
    private static final String CLIENT_SECRET_KEY = "IGDB_CLIENT_SECRET";
    private static final String TOKEN_KEY = "IGDB_ACCESS_TOKEN";
    private static final String TOKEN_EXP_KEY = "IGDB_TOKEN_EXPIRATION";
    private static final String TOKEN_URL = "https://id.twitch.tv/oauth2/token";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAccessToken() {
        String token = apiConfigService.getValor(TOKEN_KEY);
        String expStr = apiConfigService.getValor(TOKEN_EXP_KEY);

        if (token.isEmpty() || expStr.isEmpty() || Instant.now().isAfter(Instant.ofEpochSecond(Long.parseLong(expStr)))) {
            token = renovarToken();
        }
        return token;
    }

    private String renovarToken() {
        try {
            // 🔹 Sempre pega valores descriptografados do banco
            String clientId = apiConfigService.getValor(CLIENT_ID_KEY);
            String clientSecret = apiConfigService.getValor(CLIENT_SECRET_KEY);

            if (clientId.isEmpty() || clientSecret.isEmpty()) {
                throw new RuntimeException("IGDB_CLIENT_ID ou IGDB_CLIENT_SECRET não configurados no banco!");
            }

            String url = TOKEN_URL + "?client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("Accept", "application/json")
                    .build();

            System.out.println("Tentando renovar token com:");
            System.out.println("clientId: " + clientId);
            System.out.println("clientSecret: " + clientSecret);
            System.out.println("URL: " + url);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> map = objectMapper.readValue(response.body(), Map.class);

            String accessToken = (String) map.get("access_token");
            Integer expiresIn = (Integer) map.get("expires_in");
            long expirationEpoch = Instant.now().getEpochSecond() + expiresIn - 60; // margem de 1 minuto

            // 🔹 Salva token criptografado
            apiConfigService.atualizarValor(TOKEN_KEY, accessToken);
            apiConfigService.atualizarValor(TOKEN_EXP_KEY, String.valueOf(expirationEpoch));

            return accessToken;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao renovar token IGDB", e);
        }
    }
}
