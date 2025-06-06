package com.wagnerdf.fancollectorsmedia.cadastro;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.CadastroHobby;
import com.wagnerdf.fancollectorsmedia.model.Endereco;
import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.model.Papel;
import com.wagnerdf.fancollectorsmedia.model.Usuario;
import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;
import com.wagnerdf.fancollectorsmedia.repository.CadastroHobbyRepository;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.HobbyRepository;
import com.wagnerdf.fancollectorsmedia.repository.PapelRepository;
import com.wagnerdf.fancollectorsmedia.repository.UsuarioRepository;

@SpringBootTest
public class CadastroCompletoIntegrationTest {

    @Autowired
    private CadastroRepository cadastroRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private CadastroHobbyRepository cadastroHobbyRepository;

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Test
    public void deveCadastrarUsuarioComHobbiesExistentesEPapelDefault() {
        // 1. Criar Endereco (sem salvar manualmente)
        Endereco endereco = new Endereco();
        endereco.setRua("QNO 214 Conjunto Y");
        endereco.setNumero("98");
        endereco.setComplemento("Casa");
        endereco.setBairro("Sol Nascente");
        endereco.setCidade("Brasília");
        endereco.setEstado("DF");
        endereco.setCep("72350074");

        // 2. Criar Cadastro e associar o Endereco
        Cadastro cadastro = new Cadastro();
        cadastro.setNome("Joao");
        cadastro.setSobreNome("Silva");
        cadastro.setDataNascimento(LocalDate.of(1990, 5, 15));
        cadastro.setSexo("M");
        cadastro.setEmail("joao.silva7@example.com");
        cadastro.setTelefone("123456");
        cadastro.setEndereco(endereco); // Cascade cuidará do save
        cadastro.setStatus(StatusUsuario.ATIVO);
        cadastro.setDataCadastro(java.time.LocalDateTime.now());
        cadastro.setAvatarUrl("https://avatar.com/joao.png");
        cadastroRepository.save(cadastro);

        // 3. Buscar hobbies já existentes (IDs 2 e 4)
        Optional<Hobby> hobby2 = hobbyRepository.findById(2L);
        Optional<Hobby> hobby4 = hobbyRepository.findById(4L);
        assertThat(hobby2).isPresent();
        assertThat(hobby4).isPresent();

        CadastroHobby cadastroHobby1 = new CadastroHobby();
        cadastroHobby1.setCadastro(cadastro);
        cadastroHobby1.setHobby(hobby2.get());
        cadastroHobby1.setNivelInteresse(4);
        cadastroHobby1.setDataRegistro(java.time.LocalDateTime.now());
        cadastroHobbyRepository.save(cadastroHobby1);

        CadastroHobby cadastroHobby2 = new CadastroHobby();
        cadastroHobby2.setCadastro(cadastro);
        cadastroHobby2.setHobby(hobby4.get());
        cadastroHobby2.setNivelInteresse(3);
        cadastroHobby2.setDataRegistro(java.time.LocalDateTime.now());
        cadastroHobbyRepository.save(cadastroHobby2);

        // 4. Buscar papel com ID 1
        Optional<Papel> papelOpt = papelRepository.findById(1L);
        assertThat(papelOpt).isPresent();

        // 5. Criar Usuario
        Usuario usuario = Usuario.builder()
                .cadastro(cadastro)
                .login(cadastro.getEmail())
                .senha(passwordEncoder.encode("123456")) 
                .papel(papelOpt.get())
                .build();
        usuarioRepository.save(usuario);

        // ✅ Asserções
        assertThat(usuario.getId()).isNotNull();
        assertThat(usuario.getCadastro().getEndereco().getCidade()).isEqualTo("Brasília");
        assertThat(usuario.getPapel().getNome()).isEqualTo(papelOpt.get().getNome());
        assertThat(passwordEncoder.matches("123456", usuario.getSenha())).isTrue();

        List<CadastroHobby> hobbies = cadastroHobbyRepository.findByCadastro(cadastro);
        assertThat(hobbies).hasSize(2);
        assertThat(hobbies).extracting(h -> h.getHobby().getId()).containsExactlyInAnyOrder(2L, 4L);
    }
}
