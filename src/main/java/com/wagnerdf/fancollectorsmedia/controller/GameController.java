package com.wagnerdf.fancollectorsmedia.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GameController {

    @Value("${igdb.clientId}")
    private String clientId;

    @Value("${igdb.accessToken}")
    private String clientSecret;
    


    @GetMapping("/api/games")
    public ResponseEntity<?> buscarGames(@RequestParam String search) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 1️⃣ Gerar token do Twitch usando POST com form-urlencoded
            HttpHeaders oauthHeaders = new HttpHeaders();
            oauthHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String oauthBody = "client_id=" + clientId +
                               "&client_secret=" + clientSecret +
                               "&grant_type=client_credentials";

            HttpEntity<String> oauthRequest = new HttpEntity<>(oauthBody, oauthHeaders);

            ResponseEntity<Map> oauthResponse = restTemplate.postForEntity(
                    "https://id.twitch.tv/oauth2/token",
                    oauthRequest,
                    Map.class
            );

            if (oauthResponse.getBody() == null || !oauthResponse.getBody().containsKey("access_token")) {
                return ResponseEntity.status(500)
                        .body(Map.of("error", "Não foi possível gerar o token do Twitch"));
            }

            String accessToken = (String) oauthResponse.getBody().get("access_token");

            // 2️⃣ Preparar requisição para a IGDB
            String igdbUrl = "https://api.igdb.com/v4/games";
            String body = "search \"" + search + "\"; fields id,name,first_release_date,cover.url; limit 10;";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Client-ID", clientId);
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.TEXT_PLAIN);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            // 3️⃣ Chamar IGDB
            ResponseEntity<String> response = restTemplate.exchange(
                    igdbUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Erro ao buscar jogos", "details", e.getMessage()));
        }
    }
}
