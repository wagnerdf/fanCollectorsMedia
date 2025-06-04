package com.wagnerdf.fancollectorsmedia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.dto.AuthRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.AuthResponseDto;
import com.wagnerdf.fancollectorsmedia.dto.RegisterRequestDto;
import com.wagnerdf.fancollectorsmedia.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
		return ResponseEntity.ok(authService.register(request));
	}
}
