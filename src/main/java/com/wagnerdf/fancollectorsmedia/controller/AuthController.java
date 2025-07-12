package com.wagnerdf.fancollectorsmedia.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wagnerdf.fancollectorsmedia.dto.AuthRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.AuthResponseDto;
import com.wagnerdf.fancollectorsmedia.dto.CadastroRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.RefreshTokenRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.RegisterRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.ResetarSenhaRequest;
import com.wagnerdf.fancollectorsmedia.model.PasswordResetToken;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.PasswordResetTokenRepository;
import com.wagnerdf.fancollectorsmedia.security.CustomUserDetailsService;
import com.wagnerdf.fancollectorsmedia.security.JwtService;
import com.wagnerdf.fancollectorsmedia.service.AuthService;
import com.wagnerdf.fancollectorsmedia.service.EmailService;
import com.wagnerdf.fancollectorsmedia.service.RecuperacaoSenhaService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@Autowired
    private JwtService jwtService;
	
	@Autowired
    private CustomUserDetailsService userDetailsService;
	
	@Autowired
    private RecuperacaoSenhaService recuperacaoSenhaService;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
	private CadastroRepository cadastroRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
	    try {
	        return ResponseEntity.ok(authService.login(request));
	    } catch (BadCredentialsException e) {
	        AuthResponseDto response = new AuthResponseDto(null, e.getMessage());
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    }
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDto request) {
	    String refreshToken = request.getRefreshToken();

	    try {
	        // Valida o refresh token
	        if (!jwtService.isRefreshTokenValid(refreshToken)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body(Map.of("error", "Refresh token inválido ou expirado."));
	        }

	        String username = jwtService.extractUsername(refreshToken);
	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	        String novoAccessToken = jwtService.generateToken(userDetails);

	        Map<String, String> response = new HashMap<>();
	        response.put("accessToken", novoAccessToken);

	        return ResponseEntity.ok(response);

	    } catch (ExpiredJwtException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("error", "Refresh token expirado. Faça login novamente."));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(Map.of("error", "Erro ao processar o refresh token."));
	    }
	}

	@PostMapping("/registerFull")
	public ResponseEntity<AuthResponseDto> registerFull(@Valid @RequestBody CadastroRequestDto request) {
	    return ResponseEntity.ok(authService.registerFull(request));
	}
	
	@Transactional
	@PostMapping("/recuperar-senha")
	public String recuperarSenha(@RequestBody Map<String, String> body) {
	    String email = body.get("email");

	    if (!cadastroRepository.existsByEmail(email)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail não encontrado.");
	    }

	    // Verifica se já existe um token válido
	    Optional<PasswordResetToken> tokenExistente = passwordResetTokenRepository
	            .findByEmailAndExpiracaoAfter(email, LocalDateTime.now());

	    if (tokenExistente.isPresent()) {
	        return "Já existe um pedido de redefinição de senha em andamento. Verifique seu e-mail.";
	    }

	    String token = UUID.randomUUID().toString();
	    LocalDateTime expiracao = LocalDateTime.now().plusMinutes(30);

	    passwordResetTokenRepository.deleteByEmail(email);

	    PasswordResetToken resetToken = new PasswordResetToken(email, token, expiracao);
	    passwordResetTokenRepository.save(resetToken);

	    emailService.enviarEmailRecuperacao(email, token);

	    return "E-mail de recuperação enviado com sucesso!";
	}


    
    @PostMapping("/resetar-senha")
    public ResponseEntity<String> resetarSenha(@RequestBody ResetarSenhaRequest request) {
        try {
            recuperacaoSenhaService.redefinirSenha(request.getToken(), request.getNovaSenha());
            return ResponseEntity.ok("Senha redefinida com sucesso.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }


}
