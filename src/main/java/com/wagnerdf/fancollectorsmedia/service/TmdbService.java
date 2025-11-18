package com.wagnerdf.fancollectorsmedia.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TmdbService {

    @Value("${REACT_APP_API_TMDB}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private String traduzirTipo(String tipo) {
        return switch (tipo) {
            case "movie" -> "Filme";
            case "tv" -> "Série";
            default -> tipo;
        };
    }

    public List<Map<String, Object>> buscarMidias(String query) {

        try {
            String apiKey = System.getenv("REACT_APP_API_TMDB");

            // URL TMDB
            String url = "https://api.themoviedb.org/3/search/multi?query=" + 
                    URLEncoder.encode(query, StandardCharsets.UTF_8) + 
                    "&api_key=" + apiKey + 
                    "&language=pt-BR";

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            List<Map<String, Object>> listaFinal = new ArrayList<>();

            for (Map<String, Object> item : results) {

                String mediaType = (String) item.get("media_type");
                if (mediaType == null) continue;

                // Só aceitaremos movie e tv
                if (!mediaType.equals("movie") && !mediaType.equals("tv")) continue;

                String tituloAlternativo;
                String ano;

                // Filme
                if (mediaType.equals("movie")) {
                    tituloAlternativo = (String) item.get("title");

                    String releaseDate = (String) item.get("release_date");
                    ano = (releaseDate != null && releaseDate.length() >= 4)
                            ? releaseDate.substring(0, 4)
                            : "";
                }
                // Série
                else {
                    tituloAlternativo = (String) item.get("name");

                    String firstAirDate = (String) item.get("first_air_date");
                    ano = (firstAirDate != null && firstAirDate.length() >= 4)
                            ? firstAirDate.substring(0, 4)
                            : "";
                }

                Map<String, Object> novoItem = new HashMap<>();
                novoItem.put("id", item.get("id"));
                novoItem.put("titulo_alternativo", tituloAlternativo);
                novoItem.put("tipo", mediaType.equals("movie") ? "Filme" : "Série");
                novoItem.put("ano", ano);

                listaFinal.add(novoItem);
            }

            return listaFinal;

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao buscar na API do TMDB", ex);
        }
    }

}
