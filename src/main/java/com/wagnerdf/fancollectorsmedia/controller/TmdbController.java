package com.wagnerdf.fancollectorsmedia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
}


