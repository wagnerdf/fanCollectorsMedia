package com.wagnerdf.fancollectorsmedia.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Endereco;
import com.wagnerdf.fancollectorsmedia.model.Papel;
import com.wagnerdf.fancollectorsmedia.model.Usuario;
import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.EnderecoRepository;
import com.wagnerdf.fancollectorsmedia.repository.PapelRepository;
import com.wagnerdf.fancollectorsmedia.repository.UsuarioRepository;

@Component
public class UsuarioRootInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CadastroRepository cadastroRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Verifica se o usuário admin já existe
        if (!usuarioRepository.existsByLogin("admin@admin")) {

            // Cria um novo endereço exclusivo para o admin
            Endereco endereco = new Endereco();
            endereco.setRua("Rua do Admin");
            endereco.setNumero("100");
            endereco.setBairro("Centro");
            endereco.setCidade("Brasília");
            endereco.setEstado("DF");
            endereco.setCep("72220-049");
            endereco = enderecoRepository.save(endereco);

            // Cria e salva o cadastro do admin
            Cadastro cadastro = new Cadastro();
            cadastro.setNome("Administrador");
            cadastro.setSobreNome("Root");
            cadastro.setEmail("admin@admin");
            cadastro.setDataNascimento(LocalDate.parse("1985-10-10"));
            cadastro.setDataCadastro(LocalDateTime.now());
            cadastro.setSexo("MASCULINO");
            cadastro.setStatus(StatusUsuario.ATIVO);
            cadastro.setTelefone("61998842114");
            cadastro.setEndereco(endereco);
            cadastro = cadastroRepository.save(cadastro);

            // Busca o papel ROLE_ADMIN (id = 2)
            Papel adminRole = papelRepository.findById(2L)
                    .orElseThrow(() -> new RuntimeException("Papel ROLE_ADMIN não encontrado"));

            // Cria e salva o usuário root
            Usuario usuario = new Usuario();
            usuario.setLogin("admin@admin");
            usuario.setSenha(passwordEncoder.encode("admin"));
            usuario.setCadastro(cadastro);
            usuario.setPapel(adminRole);
            usuarioRepository.save(usuario);

            System.out.println("Usuário root criado com ROLE_ADMIN!");
        }
    }
}
