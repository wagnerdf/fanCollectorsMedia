package com.wagnerdf.fancollectorsmedia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wagnerdf.fancollectorsmedia.service.IgdbService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@RestController
@RequestMapping("/api/igdb")
public class IgdbController {

    @Autowired
    private IgdbService igdbService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ------------------ REFRESH MANUAL ------------------
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken() {
        try {
            String newToken = igdbService.refreshToken();
            return ResponseEntity.ok(Map.of(
                    "accessToken", newToken,
                    "message", "Token IGDB atualizado com sucesso"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // ------------------ BUSCA BÁSICA DE GAMES ------------------
    @PostMapping("/games/basica")
    public ResponseEntity<?> buscaBasicaGames(@RequestBody Map<String, String> request) {
        try {
            String nomeBusca = request.getOrDefault("nome", "").trim();
            if (nomeBusca.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Campo 'nome' é obrigatório"));
            }

            String response = igdbService.buscaBasicaGames(nomeBusca);
            Object json = objectMapper.readValue(response, Object.class);
            return ResponseEntity.ok(json);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
