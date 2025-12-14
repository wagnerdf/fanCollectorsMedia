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

@Service
public class TmdbService {

    @Value("${REACT_APP_API_TMDB}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // --------------------------------------------------------------
    // ----------------- BUSCAR MÍDIAS (LISTAGEM) -------------------
    // --------------------------------------------------------------
    public List<Map<String, Object>> buscarMidias(String query) {

        try {

            String url = "https://api.themoviedb.org/3/search/multi?query=" +
                    URLEncoder.encode(query, StandardCharsets.UTF_8) +
                    "&api_key=" + apiKey +
                    "&language=pt-BR";

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<Map<String, Object>> listaFinal = new ArrayList<>();

            for (Map<String, Object> item : results) {

                String mediaType = (String) item.get("media_type");
                if (mediaType == null) continue;

                // Aceitar apenas movie ou tv
                if (!mediaType.equals("movie") && !mediaType.equals("tv")) continue;

                String tituloAlternativo;
                String ano;

                if (mediaType.equals("movie")) {
                    tituloAlternativo = (String) item.get("title");
                    String releaseDate = (String) item.get("release_date");

                    ano = (releaseDate != null && releaseDate.length() >= 4)
                            ? releaseDate.substring(0, 4) : "";

                } else {
                    tituloAlternativo = (String) item.get("name");
                    String firstAirDate = (String) item.get("first_air_date");

                    ano = (firstAirDate != null && firstAirDate.length() >= 4)
                            ? firstAirDate.substring(0, 4) : "";
                }

                Map<String, Object> novoItem = new HashMap<>();
                novoItem.put("id", item.get("id"));
                novoItem.put("titulo_alternativo", tituloAlternativo);
                novoItem.put("tipo", mediaType.equals("movie") ? "Filme" : "Série");
                novoItem.put("media_type", mediaType); // 🔥 ESSENCIAL
                novoItem.put("ano", ano);

                listaFinal.add(novoItem);
            }

            return listaFinal;

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao buscar na API do TMDB", ex);
        }
    }

    // --------------------------------------------------------------
    // --------------- BUSCAR DETALHES (COM CREDITS) ----------------
    // --------------------------------------------------------------
    public Map<String, Object> buscarDetalhes(Integer id, String mediaType) {
        try {
            String tipoEndpoint = mediaType.equals("movie") ? "movie" : "tv";
            String tipoMidia = mediaType.equals("movie") ? "Filme" : "Série";

            String baseUrl = "https://api.themoviedb.org/3/" + tipoEndpoint + "/" + id +
                    "?api_key=" + apiKey + "&language=pt-BR";

            String creditsUrl = "https://api.themoviedb.org/3/" + tipoEndpoint + "/" + id +
                    "/credits?api_key=" + apiKey + "&language=pt-BR";

            Map<String, Object> dados =
                    restTemplate.getForObject(baseUrl, Map.class);

            Map<String, Object> creditos =
                    restTemplate.getForObject(creditsUrl, Map.class);

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

            // -------------------------------
            // ANO DE LANÇAMENTO
            // -------------------------------
            String data = mediaType.equals("movie")
                    ? (String) dados.get("release_date")
                    : (String) dados.get("first_air_date");

            retorno.put("ano_lancamento",
                    (data != null && data.length() >= 4) ? data.substring(0, 4) : "");

            // -------------------------------
            // GÊNEROS
            // -------------------------------
            List<Map<String, Object>> generos = (List<Map<String, Object>>) dados.get("genres");
            List<String> nomesGeneros = new ArrayList<>();

            if (generos != null) {
                for (Map<String, Object> g : generos) {
                    nomesGeneros.add((String) g.get("name"));
                }
            }
            retorno.put("generos", nomesGeneros);

            // -------------------------------
            // DURAÇÃO
            // -------------------------------
            if (mediaType.equals("movie")) {
                retorno.put("duracao", dados.get("runtime"));
            } else {
                List<Integer> epTimes = (List<Integer>) dados.get("episode_run_time");
                retorno.put("duracao",
                        (epTimes != null && !epTimes.isEmpty()) ? epTimes.get(0) : null);
            }

            // -------------------------------
            // ESTÚDIOS
            // -------------------------------
            List<Map<String, Object>> companias =
                    (List<Map<String, Object>>) dados.get("production_companies");

            List<String> nomesEstudios = new ArrayList<>();
            if (companias != null) {
                for (Map<String, Object> e : companias) {
                    nomesEstudios.add((String) e.get("name"));
                }
            }
            retorno.put("estudio", nomesEstudios);

            // -------------------------------
            // ARTISTAS
            // -------------------------------
            List<Map<String, Object>> cast =
                    (List<Map<String, Object>>) creditos.get("cast");

            List<String> artistas = new ArrayList<>();
            if (cast != null) {
                for (int i = 0; i < Math.min(10, cast.size()); i++) {
                    artistas.add((String) cast.get(i).get("name"));
                }
            }
            retorno.put("artistas", artistas);

            // -------------------------------
            // DIRETORES
            // -------------------------------
            List<Map<String, Object>> crew =
                    (List<Map<String, Object>>) creditos.get("crew");

            List<String> diretores = new ArrayList<>();
            if (crew != null) {
                for (Map<String, Object> member : crew) {
                    if ("Director".equals(member.get("job"))) {
                        diretores.add((String) member.get("name"));
                    }
                }
            }
            retorno.put("diretores", diretores);

            return retorno;

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao buscar detalhes da mídia", ex);
        }
    }
}
