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
    
    public Map<String, Object> buscarDetalhes(Integer id) {
        try {
            String apiKey = System.getenv("REACT_APP_API_TMDB");

            // Tentar como FILME
            String movieUrl = "https://api.themoviedb.org/3/movie/" + id +
                    "?api_key=" + apiKey + "&language=pt-BR";

            // Tentar como SERIE
            String tvUrl = "https://api.themoviedb.org/3/tv/" + id +
                    "?api_key=" + apiKey + "&language=pt-BR";

            Map<String, Object> dados = null;
            String tipoMidia = null;

            try {
                dados = restTemplate.getForObject(movieUrl, Map.class);
                tipoMidia = "Filme";
            } catch (Exception e) {
                try {
                    dados = restTemplate.getForObject(tvUrl, Map.class);
                    tipoMidia = "Série";
                } catch (Exception ex2) {
                    throw new RuntimeException("Mídia não encontrada no TMDB");
                }
            }

            Map<String, Object> retorno = new HashMap<>();

            retorno.put("formato_midia", tipoMidia);

            retorno.put("titulo_original", 
                    dados.get("original_title") != null ? dados.get("original_title")
                                                         : dados.get("original_name"));

            retorno.put("titulo_alternativo",
                    dados.get("title") != null ? dados.get("title")
                                               : dados.get("name"));

            retorno.put("sinopse", dados.get("overview"));
            retorno.put("capa_url", "https://image.tmdb.org/t/p/w500" + dados.get("poster_path"));
            retorno.put("nota_media", dados.get("vote_average"));
            retorno.put("linguagem", dados.get("original_language"));
            retorno.put("classificacao_etaria", 
                    dados.get("adult") != null && (boolean) dados.get("adult") ? "18+" : "Livre");

            // Ano de lançamento
            String data = tipoMidia.equals("Filme")
                    ? (String) dados.get("release_date")
                    : (String) dados.get("first_air_date");

            retorno.put("ano_lancamento",
                    (data != null && data.length() >= 4) ? data.substring(0, 4) : "");

            // Gêneros
            List<Map<String, Object>> generos = (List<Map<String, Object>>) dados.get("genres");
            List<String> nomesGeneros = new ArrayList<>();
            if (generos != null) {
                for (Map<String, Object> g : generos) {
                    nomesGeneros.add((String) g.get("name"));
                }
            }
            retorno.put("generos", nomesGeneros);

            // Duração
            if (tipoMidia.equals("Filme")) {
                retorno.put("duracao", dados.get("runtime"));
            } else {
                retorno.put("duracao", dados.get("episode_run_time"));
            }

            // Estúdio
            List<Map<String, Object>> companias =
                    (List<Map<String, Object>>) dados.get("production_companies");

            List<String> nomesEstudios = new ArrayList<>();
            if (companias != null) {
                for (Map<String, Object> e : companias) {
                    nomesEstudios.add((String) e.get("name"));
                }
            }
            retorno.put("estudio", nomesEstudios);

            return retorno;

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao buscar detalhes da mídia", ex);
        }
    }


}
