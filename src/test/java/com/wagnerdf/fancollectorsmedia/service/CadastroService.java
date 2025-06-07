package com.wagnerdf.fancollectorsmedia.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.CadastroHobby;
import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.model.Papel;
import com.wagnerdf.fancollectorsmedia.model.Usuario;
import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;
import com.wagnerdf.fancollectorsmedia.repository.CadastroHobbyRepository;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.HobbyRepository;
import com.wagnerdf.fancollectorsmedia.repository.PapelRepository;
import com.wagnerdf.fancollectorsmedia.repository.UsuarioRepository;

@Service
public class CadastroService {

    @Autowired
    private CadastroRepository cadastroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private CadastroHobbyRepository cadastroHobbyRepository;

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuarioCompleto(Cadastro cadastro, List<Long> hobbyIds, String senha) {
        cadastro.setStatus(StatusUsuario.ATIVO);
        cadastro.setDataCadastro(LocalDateTime.now());

        Cadastro cadastroSalvo = cadastroRepository.save(cadastro);

        for (Long hobbyId : hobbyIds) {
            Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
            hobbyOpt.ifPresent(hobby -> {
                CadastroHobby ch = new CadastroHobby();
                ch.setCadastro(cadastroSalvo);
                ch.setHobby(hobby);
                ch.setNivelInteresse(3); // padrão
                ch.setDataRegistro(LocalDateTime.now());
                cadastroHobbyRepository.save(ch);
            });
        }

        Papel papel = papelRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Papel padrão não encontrado"));

        String senhaCodificada = passwordEncoder.encode(senha);

        Usuario usuario = Usuario.builder()
                .cadastro(cadastroSalvo)
                .login(cadastro.getEmail())
                .senha(senhaCodificada)
                .papel(papel)
                .build();

        return usuarioRepository.save(usuario);
    }
}

