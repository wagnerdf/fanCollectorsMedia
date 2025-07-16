package com.wagnerdf.fancollectorsmedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.dto.CadastroHobbyDto;
import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.model.Usuario;
import com.wagnerdf.fancollectorsmedia.service.HobbyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hobbies")
public class HobbyController {

    @Autowired
    private HobbyService hobbyService;

    @GetMapping
    public List<Hobby> listarTodos() {
        return hobbyService.listarTodos();
    }
    
    @PostMapping
    public ResponseEntity<Void> cadastrarHobby(@RequestBody @Valid CadastroHobbyDto dto,
                                               @AuthenticationPrincipal Usuario usuarioLogado) {
        hobbyService.cadastrarHobby(usuarioLogado.getLogin(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PostMapping("/lista")
    public ResponseEntity<Void> cadastrarMultiplosHobbies(@RequestBody @Valid List<CadastroHobbyDto> dtoList,
                                                          @AuthenticationPrincipal Usuario usuarioLogado) {
        hobbyService.cadastrarMultiplosHobbies(usuarioLogado.getLogin(), dtoList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
