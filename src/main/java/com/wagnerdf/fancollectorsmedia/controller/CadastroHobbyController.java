package com.wagnerdf.fancollectorsmedia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.wagnerdf.fancollectorsmedia.dto.CadastroHobbyDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.CadastroHobby;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.service.CadastroHobbyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cadastro-hobby")
public class CadastroHobbyController {

    @Autowired
    private CadastroHobbyService cadastroHobbyService;

    @Autowired
    private CadastroRepository cadastroRepository;

    // 🔒 Método reutilizável para validar se o cadastroHobby pertence ao usuário autenticado
    private ResponseEntity<?> validarProprietario(Long cadastroHobbyId, UserDetails userDetails) {
        Optional<Cadastro> optCadastro = cadastroRepository.findByEmail(userDetails.getUsername());

        if (optCadastro.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        Optional<CadastroHobby> opt = cadastroHobbyService.buscarPorId(cadastroHobbyId);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CadastroHobby não encontrado.");
        }

        CadastroHobby cadastroHobby = opt.get();
        if (!cadastroHobby.getCadastro().getId().equals(optCadastro.get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para modificar este hobby.");
        }

        return null; // OK
    }

    @GetMapping("/meus")
    public ResponseEntity<List<CadastroHobbyDto>> listarMeusHobbies(@AuthenticationPrincipal UserDetails userDetails) {
        return cadastroRepository.findByEmail(userDetails.getUsername())
            .map(cadastro -> {
                List<CadastroHobbyDto> hobbies = cadastroHobbyService.listarPorCadastroId(cadastro.getId());
                return ResponseEntity.ok(hobbies);
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<CadastroHobbyDto> adicionarHobby(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CadastroHobbyDto dto) {

        Optional<Cadastro> cadastroOpt = cadastroRepository.findByEmail(userDetails.getUsername());
        if (cadastroOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Garante que o usuário só crie hobbies para ele mesmo
        if (!dto.getCadastroId().equals(cadastroOpt.get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CadastroHobbyDto criado = cadastroHobbyService.adicionar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarInteresse(
            @PathVariable Long id,
            @RequestBody @Valid CadastroHobbyDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        ResponseEntity<?> validacao = validarProprietario(id, userDetails);
        if (validacao != null) return validacao;

        CadastroHobbyDto atualizado = cadastroHobbyService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        ResponseEntity<?> validacao = validarProprietario(id, userDetails);
        if (validacao != null) return validacao;

        cadastroHobbyService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
