package com.wagnerdf.fancollectorsmedia.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerdf.fancollectorsmedia.dto.AuthRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.AuthResponseDto;
import com.wagnerdf.fancollectorsmedia.dto.CadastroRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.EnderecoDto;
import com.wagnerdf.fancollectorsmedia.dto.RegisterRequestDto;
import com.wagnerdf.fancollectorsmedia.exception.EmailDuplicadoException;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Endereco;
import com.wagnerdf.fancollectorsmedia.model.Papel;
import com.wagnerdf.fancollectorsmedia.model.Usuario;
import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.PapelRepository;
import com.wagnerdf.fancollectorsmedia.repository.UsuarioRepository;
import com.wagnerdf.fancollectorsmedia.security.JwtService;

@Service
public class AuthService {
	
	@Autowired
	private CadastroRepository cadastroRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PapelRepository papelRepository;
	
	@Autowired
	private UserDetailsService userDetailsService;

	public AuthResponseDto login(AuthRequestDto request) {
	    try {
	        authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(request.getLogin(), request.getSenha())
	        );

	        UserDetails user = userDetailsService.loadUserByUsername(request.getLogin());
	        String token = jwtService.generateToken(user);

	        return new AuthResponseDto(token, "Login efetuado com sucesso");

	    } catch (AuthenticationException e) {
	        throw new BadCredentialsException("Login ou senha incorretos");
	    }
	}


	public AuthResponseDto register(RegisterRequestDto request) {

		if (usuarioRepository.findByLogin(request.getLogin()).isPresent()) {
			throw new EmailDuplicadoException("E-mail já está em uso");
		}

		Papel papel = papelRepository.findByNome("ROLE_USER")
				.orElseThrow(() -> new RuntimeException("Papel ROLE_USER não encontrado"));
		Usuario usuario = Usuario.builder().login(request.getLogin()) // agora o login é o campo onde guardamos o e-mail
				.senha(passwordEncoder.encode(request.getSenha())).papel(papel).build();

		usuarioRepository.save(usuario);
		String token = jwtService.generateToken(usuario);
		return new AuthResponseDto(token, "Usuário registrado com sucesso");
	}
	
	@Transactional
	public AuthResponseDto registerFull(CadastroRequestDto request) {
		String emailLower = request.getEmail().toLowerCase();

		if (usuarioRepository.existsByLoginIgnoreCase(emailLower)) {
		    throw new EmailDuplicadoException("Este e-mail já está cadastrado.");
		}

	    Papel papel = papelRepository.findByNome("ROLE_USER")
	            .orElseThrow(() -> new RuntimeException("Papel ROLE_USER não encontrado"));

	    // Criar cadastro (dados pessoais + endereço)
	    Cadastro cadastro = Cadastro.builder()
	        .nome(request.getNome())
	        .sobreNome(request.getSobreNome())
	        .dataNascimento(request.getDataNascimento())
	        .sexo(request.getSexo())
	        .telefone(request.getTelefone())
	        .email(emailLower)
	        .dataCadastro(LocalDateTime.now())
	        .status(StatusUsuario.ATIVO)
	        .avatarUrl(request.getAvatarUrl())
	        .endereco(EnderecoMapper.toEntity(request.getEndereco()))
	        .build();

	    cadastroRepository.save(cadastro);

	    // Criar usuário para login
	    Usuario usuario = Usuario.builder()
	    	.login(emailLower)
	        .senha(passwordEncoder.encode(request.getSenha()))
	        .papel(papel)
	        .cadastro(cadastro) // associa o usuário ao cadastro
	        .build();

	    usuarioRepository.save(usuario);

	    // Gerar token JWT com o usuário criado
	    String token = jwtService.generateToken(usuario);

	    return new AuthResponseDto(token, "Cadastro completo e usuário criado com sucesso");
	}
	
	public class EnderecoMapper {
	    public static Endereco toEntity(EnderecoDto dto) {
	        return Endereco.builder()
	            .cep(dto.getCep())
	            .rua(dto.getRua())
	            .numero(dto.getNumero())
	            .complemento(dto.getComplemento())
	            .bairro(dto.getBairro())
	            .cidade(dto.getCidade())
	            .estado(dto.getEstado())
	            .build();
	    }
	}

}
