package com.wagnerdf.fancollectorsmedia.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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

    // ------------------ OBTÉM TOKEN VÁLIDO ------------------
    public String getAccessToken() {
        String token = apiConfigService.getValor(TOKEN_KEY);
        String expStr = apiConfigService.getValor(TOKEN_EXP_KEY);

        boolean tokenExpirado = true;
        if (!token.isEmpty() && !expStr.isEmpty()) {
            Instant expiration = Instant.ofEpochSecond(Long.parseLong(expStr));
            tokenExpirado = Instant.now().isAfter(expiration);
        }

        if (token.isEmpty() || tokenExpirado) {
            token = renovarToken();
        }

        return token;
    }

    // ------------------ REFRESH MANUAL ------------------
    public String refreshToken() {
        return renovarToken();
    }

    // ------------------ BUSCA BÁSICA DE GAMES ------------------
    public String buscaBasicaGames(String nomeBusca) {
        try {
            // 🔹 Query para buscar até 10 games pelo nome
            String query = "fields name,platforms,cover,first_release_date;"
                         + " search \"" + nomeBusca + "\";"
                         + " limit 10;";

            String gamesResponse = callIgdbApi("games", query);
            List<Map<String, Object>> games = objectMapper.readValue(gamesResponse, List.class);

            // 🔹 Coletar todos IDs de plataformas
            Set<Integer> allPlatformIds = new HashSet<>();
            for (Map<String, Object> game : games) {
                if (game.containsKey("platforms")) {
                    List<Integer> ids = (List<Integer>) game.get("platforms");
                    allPlatformIds.addAll(ids);
                }
            }

            // 🔹 Buscar nomes de plataformas em uma única chamada
            Map<Integer, String> platformIdToName = new HashMap<>();
            if (!allPlatformIds.isEmpty()) {
                String ids = allPlatformIds.stream().map(String::valueOf).collect(Collectors.joining(","));
                String platformResp = callIgdbApi("platforms", "fields id,name; where id = (" + ids + ");");
                List<Map<String, Object>> platforms = objectMapper.readValue(platformResp, List.class);
                for (Map<String, Object> p : platforms) {
                    platformIdToName.put((Integer) p.get("id"), (String) p.get("name"));
                }
            }

            // 🔹 Processar games: ID, nome, capa, ano e plataformas
            for (Map<String, Object> game : games) {
                game.put("id", game.get("id"));

                // CAPA
                if (game.containsKey("cover")) {
                    Integer coverId = (Integer) game.get("cover");
                    if (coverId != null) {
                        String coverResponse = callIgdbApi("covers", "fields image_id; where id = " + coverId + ";");
                        List<Map<String, Object>> covers = objectMapper.readValue(coverResponse, List.class);
                        if (!covers.isEmpty()) {
                            String imageId = (String) covers.get(0).get("image_id");
                            String coverUrl = "https://images.igdb.com/igdb/image/upload/t_cover_big/" + imageId + ".jpg";
                            game.put("cover_url", coverUrl);
                        }
                    }
                }

                // ANO
                if (game.containsKey("first_release_date")) {
                    Integer ts = (Integer) game.get("first_release_date");
                    if (ts != null) {
                        int year = Instant.ofEpochSecond(ts).atZone(ZoneId.systemDefault()).getYear();
                        game.put("year", year);
                    }
                }

                // PLATAFORMAS
                if (game.containsKey("platforms")) {
                    List<Integer> platformIds = (List<Integer>) game.get("platforms");
                    List<String> platformNames = platformIds.stream()
                            .map(platformIdToName::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    game.put("platforms_name", platformNames);
                }

                // 🔹 Remover campos desnecessários para busca básica
                game.keySet().retainAll(Set.of("id", "name", "year", "cover_url", "platforms_name"));
            }

            return objectMapper.writeValueAsString(games);

        } catch (Exception e) {
            throw new RuntimeException("Erro na busca básica de games: " + e.getMessage(), e);
        }
    }

    // ------------------ CHAMADA GENÉRICA COM REFRESH AUTOMÁTICO ------------------
    private String callIgdbApi(String endpoint, String body) {
        try {
            String token = getAccessToken();
            String clientId = apiConfigService.getValor(CLIENT_ID_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.igdb.com/v4/" + endpoint))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Client-ID", clientId)
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 🔹 Se retornar 401, renova token e tenta novamente
            if (response.statusCode() == 401) {
                token = renovarToken();
                request = HttpRequest.newBuilder()
                        .uri(new URI("https://api.igdb.com/v4/" + endpoint))
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .header("Client-ID", clientId)
                        .header("Authorization", "Bearer " + token)
                        .header("Accept", "application/json")
                        .build();
                response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            }

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao chamar IGDB API: " + e.getMessage(), e);
        }
    }

    // ------------------ RENOVA TOKEN ------------------
    private String renovarToken() {
        try {
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

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            Map<String, Object> map = objectMapper.readValue(response.body(), Map.class);

            String accessToken = (String) map.get("access_token");
            Integer expiresIn = (Integer) map.get("expires_in");
            long expirationEpoch = Instant.now().getEpochSecond() + expiresIn - 60;

            apiConfigService.atualizarValor(TOKEN_KEY, accessToken);
            apiConfigService.atualizarValor(TOKEN_EXP_KEY, String.valueOf(expirationEpoch));

            return accessToken;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao renovar token IGDB: " + e.getMessage(), e);
        }
    }
}
