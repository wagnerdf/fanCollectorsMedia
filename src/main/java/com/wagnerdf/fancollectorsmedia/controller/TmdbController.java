package com.wagnerdf.fancollectorsmedia.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.service.TmdbService;

@RestController
@RequestMapping("/api/tmdb")
@CrossOrigin("*")
public class TmdbController {

    private final TmdbService tmdbService;

    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/buscar/{query}")
    public ResponseEntity<?> buscar(@PathVariable String query) {
        return ResponseEntity.ok(tmdbService.buscarMidias(query));
    }	
    
    @GetMapping("/detalhes/{id}")
    public Map<String, Object> detalhes(
            @PathVariable Integer id,
            @RequestParam String tipo
    ) {
        return tmdbService.buscarDetalhes(id, tipo);
    }


}


