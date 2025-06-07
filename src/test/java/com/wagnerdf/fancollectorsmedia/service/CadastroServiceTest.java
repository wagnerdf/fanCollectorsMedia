package com.wagnerdf.fancollectorsmedia.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Endereco;
import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.model.Papel;
import com.wagnerdf.fancollectorsmedia.model.Usuario;
import com.wagnerdf.fancollectorsmedia.repository.CadastroHobbyRepository;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.HobbyRepository;
import com.wagnerdf.fancollectorsmedia.repository.PapelRepository;
import com.wagnerdf.fancollectorsmedia.repository.UsuarioRepository;

public class CadastroServiceTest {

	@Mock
	private CadastroRepository cadastroRepository;

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private HobbyRepository hobbyRepository;

	@Mock
	private CadastroHobbyRepository cadastroHobbyRepository;

	@Mock
	private PapelRepository papelRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private CadastroService cadastroService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void deveCadastrarUsuarioCompletoComService() {
		// 1. Criar endereço
		Endereco endereco = new Endereco();
		endereco.setRua("Rua A");
		endereco.setNumero("123");
		endereco.setComplemento("Casa");
		endereco.setBairro("Centro");
		endereco.setCidade("Brasília");
		endereco.setEstado("DF");
		endereco.setCep("70000000");

		// 2. Criar cadastro
		Cadastro cadastro = new Cadastro();
		cadastro.setNome("Maria");
		cadastro.setSobreNome("Souza");
		cadastro.setDataNascimento(LocalDate.of(1995, 1, 10));
		cadastro.setSexo("F");
		cadastro.setEmail("maria.souza@example.com");
		cadastro.setTelefone("999999999");
		cadastro.setEndereco(endereco);
		cadastro.setAvatarUrl("https://avatar.com/maria.png");

		// 3. Simular retorno do cadastro salvo com ID
		Cadastro cadastroSalvo = new Cadastro();
		ReflectionTestUtils.setField(cadastroSalvo, "id", 10L);
		cadastroSalvo.setEmail(cadastro.getEmail());
		cadastroSalvo.setEndereco(endereco);

		when(cadastroRepository.save(any(Cadastro.class))).thenReturn(cadastroSalvo);

		// 4. Simular hobbies
		Hobby hobby1 = new Hobby();
		ReflectionTestUtils.setField(hobby1, "id", 2L);
		hobby1.setNome("Hobby 2");

		Hobby hobby2 = new Hobby();
		ReflectionTestUtils.setField(hobby2, "id", 4L);
		hobby2.setNome("Hobby 4");

		when(hobbyRepository.findById(2L)).thenReturn(Optional.of(hobby1));
		when(hobbyRepository.findById(4L)).thenReturn(Optional.of(hobby2));

		// 5. Simular papel ROLE_USER
		when(papelRepository.findById(1L)).thenReturn(Optional.of(new Papel(1L, "ROLE_USER")));

		// 6. Simular codificação de senha
		when(passwordEncoder.encode("senha123")).thenReturn("senhaCodificada123");

		// 7. Simular salvamento do usuário
		Usuario usuarioSalvo = Usuario.builder().cadastro(cadastroSalvo).login(cadastro.getEmail())
				.senha("senhaCodificada123").papel(new Papel(1L, "ROLE_USER")).build();

		when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

		// 8. Execução
		Usuario resultado = cadastroService.cadastrarUsuarioCompleto(cadastro, Arrays.asList(2L, 4L), "senha123");

		// 9. Verificações
		assertThat(resultado).isNotNull();
		assertThat(resultado.getLogin()).isEqualTo("maria.souza@example.com");
		assertThat(resultado.getSenha()).isEqualTo("senhaCodificada123");
		assertThat(resultado.getPapel().getNome()).isEqualTo("ROLE_USER");
	}
}
