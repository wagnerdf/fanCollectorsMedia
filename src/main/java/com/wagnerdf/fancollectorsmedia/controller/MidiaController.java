package com.wagnerdf.fancollectorsmedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import com.wagnerdf.fancollectorsmedia.dto.MidiaRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaResponseDto;
import com.wagnerdf.fancollectorsmedia.service.MidiaService;

@RestController
@RequestMapping("/api/midias")
public class MidiaController {

	@Autowired
    private MidiaService midiaService;

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
	
	@PutMapping("/{id}")
	public ResponseEntity<String> atualizarMidia(@PathVariable Long id,
	                                             @RequestBody MidiaRequestDto dto,
	                                             Authentication authentication) {
	    String username = authentication.getName();
	    midiaService.editarMidia(id, dto, username);
	    return ResponseEntity.ok("Mídia atualizada com sucesso");
	}
	
	@GetMapping("/usuario")
    public ResponseEntity<List<MidiaResponseDto>> listarMidiasDoUsuario(
            @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();
        List<MidiaResponseDto> midias = midiaService.listarMidiasDoUsuario(username);

        return ResponseEntity.ok(midias);
    }

	@GetMapping("/usuario/paginado")
	public ResponseEntity<Page<MidiaResponseDto>> listarMidiasPaginadasDoUsuario(
	        @AuthenticationPrincipal UserDetails userDetails,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size
	) {
	    String username = userDetails.getUsername();
	    Pageable pageable = PageRequest.of(page, size, Sort.by("tituloAlternativo").ascending());
	    Page<MidiaResponseDto> midias = midiaService.listarMidiasPaginadas(username, pageable);

	    return ResponseEntity.ok(midias);
	}



}
