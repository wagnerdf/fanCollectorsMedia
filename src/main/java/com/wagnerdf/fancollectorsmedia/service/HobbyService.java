package com.wagnerdf.fancollectorsmedia.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.dto.CadastroHobbyDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.CadastroHobby;
import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.repository.CadastroHobbyRepository;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.HobbyRepository;

@Service
public class HobbyService {

    @Autowired
    private HobbyRepository hobbyRepository;
    
    @Autowired
    private CadastroRepository cadastroRepository;
    
    @Autowired
    private CadastroHobbyRepository cadastroHobbyRepository;

    public List<Hobby> listarTodos() {
        return hobbyRepository.findAll();
    }
    
    public void cadastrarHobby(String email, CadastroHobbyDto dto) {
        Cadastro cadastro = cadastroRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Cadastro não encontrado"));

        Hobby hobby = hobbyRepository.findById(dto.getHobbyId())
            .orElseThrow(() -> new RuntimeException("Hobby não encontrado"));

        Optional<CadastroHobby> existente = cadastroHobbyRepository
            .findByCadastroIdAndHobbyId(cadastro.getId(), hobby.getId());

        CadastroHobby cadastroHobby = existente.orElse(new CadastroHobby());

        cadastroHobby.setCadastro(cadastro);
        cadastroHobby.setHobby(hobby);
        cadastroHobby.setNivelInteresse(dto.getNivelInteresse());
        cadastroHobby.setDataRegistro(LocalDateTime.now());

        cadastroHobbyRepository.save(cadastroHobby);
    }
    
    public void cadastrarMultiplosHobbies(String email, List<CadastroHobbyDto> dtoList) {
        Cadastro cadastro = cadastroRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Cadastro não encontrado"));

        for (CadastroHobbyDto dto : dtoList) {

            // Se nivelInteresse for nulo, apagar o hobby (se existir)
            if (dto.getNivelInteresse() == null) {
                cadastroHobbyRepository.findByCadastroIdAndHobbyId(cadastro.getId(), dto.getHobbyId())
                    .ifPresent(cadastroHobbyRepository::delete);
                continue;
            }

            // Caso contrário, criar ou atualizar normalmente
            Hobby hobby = hobbyRepository.findById(dto.getHobbyId())
                .orElseThrow(() -> new RuntimeException("Hobby não encontrado: " + dto.getHobbyId()));

            Optional<CadastroHobby> existente = cadastroHobbyRepository
                .findByCadastroIdAndHobbyId(cadastro.getId(), hobby.getId());

            CadastroHobby cadastroHobby = existente.orElse(new CadastroHobby());
            cadastroHobby.setCadastro(cadastro);
            cadastroHobby.setHobby(hobby);
            cadastroHobby.setNivelInteresse(dto.getNivelInteresse());
            cadastroHobby.setDataRegistro(LocalDateTime.now());

            cadastroHobbyRepository.save(cadastroHobby);
        }
    }



}
