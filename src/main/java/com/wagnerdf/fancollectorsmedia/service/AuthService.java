package com.wagnerdf.fancollectorsmedia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.dto.AuthRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.AuthResponseDto;
import com.wagnerdf.fancollectorsmedia.dto.RegisterRequestDto;
import com.wagnerdf.fancollectorsmedia.exception.EmailDuplicadoException;
import com.wagnerdf.fancollectorsmedia.model.Papel;
import com.wagnerdf.fancollectorsmedia.model.Usuario;
import com.wagnerdf.fancollectorsmedia.repository.PapelRepository;
import com.wagnerdf.fancollectorsmedia.repository.UsuarioRepository;
import com.wagnerdf.fancollectorsmedia.security.JwtService;

@Service
public class AuthService {

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

	public AuthResponseDto login(AuthRequestDto request) {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getSenha()));
		} catch (AuthenticationException e) {
			throw new RuntimeException("Credenciais inválidas");
		}

		Usuario usuario = usuarioRepository.findByLogin(request.getLogin())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

		String token = jwtService.generateToken(usuario);
		return new AuthResponseDto(token);
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
		return new AuthResponseDto(token);
	}
}
