package com.wagnerdf.fancollectorsmedia.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.dto.AuthRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.AuthResponseDto;
import com.wagnerdf.fancollectorsmedia.dto.RefreshTokenRequest;
import com.wagnerdf.fancollectorsmedia.dto.RegisterRequestDto;
import com.wagnerdf.fancollectorsmedia.security.CustomUserDetailsService;
import com.wagnerdf.fancollectorsmedia.security.JwtService;
import com.wagnerdf.fancollectorsmedia.service.AuthService;

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

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequest request) {
		String token = request.getToken();

		try {
			String username = jwtService.extractUsername(token);

			if (username != null && jwtService.isTokenValido(token)) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				String novoToken = jwtService.generateToken(userDetails);

				Map<String, String> response = new HashMap<>();
				response.put("token", novoToken);
				return ResponseEntity.ok(response);
			}
		} catch (ExpiredJwtException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("error", "Token expirado. Faça login novamente."));
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Token inválido."));
	}
}
