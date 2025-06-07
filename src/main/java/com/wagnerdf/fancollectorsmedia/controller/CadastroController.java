package com.wagnerdf.fancollectorsmedia.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.dto.CadastroHobbyDto;
import com.wagnerdf.fancollectorsmedia.dto.CadastroRequestDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.CadastroHobby;
import com.wagnerdf.fancollectorsmedia.model.Endereco;
import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.repository.HobbyRepository;
import com.wagnerdf.fancollectorsmedia.service.CadastroService;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/cadastros")
public class CadastroController {

    @Autowired
    private CadastroService cadastroService;

    @Autowired
    private HobbyRepository hobbyRepository;

    @PostMapping
    public ResponseEntity<Cadastro> registrarCadastro(@Valid @RequestBody CadastroRequestDto requestDto) {

        Cadastro cadastro = new Cadastro();
        cadastro.setNome(requestDto.getNome());
        cadastro.setSobreNome(requestDto.getSobreNome());
        cadastro.setDataNascimento(requestDto.getDataNascimento());
        cadastro.setSexo(requestDto.getSexo());
        cadastro.setTelefone(requestDto.getTelefone());
        cadastro.setEmail(requestDto.getEmail());
        cadastro.setAvatarUrl(requestDto.getAvatarUrl());
        cadastro.setSenha(requestDto.getSenha());

        // Endereço
        Endereco endereco = new Endereco();
        endereco.setRua(requestDto.getEndereco().getRua());
        endereco.setNumero(requestDto.getEndereco().getNumero());
        endereco.setComplemento(requestDto.getEndereco().getComplemento());
        endereco.setBairro(requestDto.getEndereco().getBairro());
        endereco.setCidade(requestDto.getEndereco().getCidade());
        endereco.setEstado(requestDto.getEndereco().getEstado());
        endereco.setCep(requestDto.getEndereco().getCep());
        endereco.setId(null); // evita erro detached entity
        cadastro.setEndereco(endereco);

        // Hobbies (opcional)
        if (requestDto.getHobbies() != null && !requestDto.getHobbies().isEmpty()) {
            List<CadastroHobby> cadastroHobbies = new ArrayList<>();
            for (CadastroHobbyDto hobbyDto : requestDto.getHobbies()) {

                // Validação nível de interesse entre 1 e 5
                int nivel = hobbyDto.getNivelInteresse();
                if (nivel < 1 || nivel > 5) {
                    throw new IllegalArgumentException("Nível de interesse deve ser entre 1 e 5.");
                }

                Hobby hobby = hobbyRepository.findById(hobbyDto.getHobbyId())
                        .orElseThrow(() -> new RuntimeException("Hobby não encontrado com ID: " + hobbyDto.getHobbyId()));

                CadastroHobby ch = new CadastroHobby();
                ch.setHobby(hobby);
                ch.setNivelInteresse(nivel);
                ch.setCadastro(cadastro);
                cadastroHobbies.add(ch);
            }
            cadastro.setHobbies(cadastroHobbies);
        }

        Cadastro cadastroSalvo = cadastroService.salvarCadastroCompleto(cadastro);
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastroSalvo);
    }
}
