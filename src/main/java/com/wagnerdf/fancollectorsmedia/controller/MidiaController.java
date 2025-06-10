package com.wagnerdf.fancollectorsmedia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.dto.MidiaRequestDto;
import com.wagnerdf.fancollectorsmedia.service.MidiaService;

@RestController
@RequestMapping("/api/midias")
public class MidiaController {

    private final MidiaService midiaService;

    public MidiaController(MidiaService midiaService) {
        this.midiaService = midiaService;
    }

    @PostMapping
    public ResponseEntity<?> criarMidia(@RequestBody MidiaRequestDto dto,
                                        Authentication authentication) {
        String username = authentication.getName(); // pega o username do token
        midiaService.salvarMidia(dto, username);
        return ResponseEntity.ok("Midia gravada com sucesso");
    }
}


