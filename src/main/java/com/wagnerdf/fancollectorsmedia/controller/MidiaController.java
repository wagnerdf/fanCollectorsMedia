package com.wagnerdf.fancollectorsmedia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.dto.MidiaRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaResponseDto;
import com.wagnerdf.fancollectorsmedia.service.MidiaService;

@RestController
@RequestMapping("/api/midias")
public class MidiaController {

	private final MidiaService midiaService;

	public MidiaController(MidiaService midiaService) {
		this.midiaService = midiaService;
	}

	@PostMapping
	public ResponseEntity<?> criarMidia(@RequestBody MidiaRequestDto dto, Authentication authentication) {
		String username = authentication.getName(); // pega o username do token
		midiaService.salvarMidia(dto, username);
		return ResponseEntity.ok("Midia gravada com sucesso");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletarMidia(@PathVariable Long id, Authentication authentication) {
		String username = authentication.getName();
		midiaService.deletarMidia(id, username);
		return ResponseEntity.ok("Mídia deletada com sucesso");
	}

	@GetMapping
	public ResponseEntity<List<MidiaResponseDto>> listarTodas(Authentication authentication) {
		String username = authentication.getName();
		List<MidiaResponseDto> midias = midiaService.listarMidiasDoUsuario(username);
		return ResponseEntity.ok(midias);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MidiaResponseDto> buscarPorId(@PathVariable Long id, Authentication authentication) {
		String username = authentication.getName();
		MidiaResponseDto midia = midiaService.buscarMidiaPorId(id, username);
		return ResponseEntity.ok(midia);
	}

}
