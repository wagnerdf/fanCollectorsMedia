package com.wagnerdf.fancollectorsmedia.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.exception.EmailDuplicadoException;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Endereco;
import com.wagnerdf.fancollectorsmedia.model.Papel;
import com.wagnerdf.fancollectorsmedia.model.Usuario;
import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;
import com.wagnerdf.fancollectorsmedia.repository.CadastroHobbyRepository;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.EnderecoRepository;
import com.wagnerdf.fancollectorsmedia.repository.PapelRepository;
import com.wagnerdf.fancollectorsmedia.repository.UsuarioRepository;

@Service
public class CadastroService {

    @Autowired
    private CadastroRepository cadastroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private CadastroHobbyRepository cadastroHobbyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Cadastro salvarCadastroCompleto(Cadastro cadastro) {
    	
    	 if (cadastroRepository.existsByEmail(cadastro.getEmail())) {
             throw new EmailDuplicadoException("Email já está em uso. Por favor, use outro email.");
    	 }
    	
        if (cadastro.getSenha() == null || cadastro.getSenha().isBlank()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }

        cadastro.setDataCadastro(LocalDateTime.now());
        cadastro.setStatus(StatusUsuario.ATIVO);

        // 🔐 Salva o endereço ANTES de setar no cadastro
        Endereco endereco = cadastro.getEndereco();
        endereco.setId(null); // 🔴 Garante que seja novo
        cadastro.setEndereco(cadastro.getEndereco()); // já está setado anteriormente

        // ✅ Salva o cadastro
        Cadastro cadastroSalvo = cadastroRepository.save(cadastro);

        // 🔐 Cria o usuário
        Papel papel = papelRepository.findByNome("ROLE_USER")
        	    .orElseThrow(() -> new RuntimeException("Papel padrão ROLE_USER não encontrado."));
        
        Usuario usuario = new Usuario();
        usuario.setLogin(cadastro.getEmail());
        usuario.setSenha(passwordEncoder.encode(cadastro.getSenha()));
        usuario.setCadastro(cadastroSalvo);
        usuario.setPapel(papel);
        usuarioRepository.save(usuario);

        // 🔄 Salva os hobbies (se houverem)
        if (cadastro.getHobbies() != null && !cadastro.getHobbies().isEmpty()) {
            cadastro.getHobbies().forEach(hobby -> {
                hobby.setCadastro(cadastroSalvo);
                cadastroHobbyRepository.save(hobby);
            });
        }

        return cadastroSalvo;
    }
}