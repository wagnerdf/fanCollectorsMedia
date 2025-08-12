package com.wagnerdf.fancollectorsmedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<?> listarMidiasDoUsuario(
	        @AuthenticationPrincipal UserDetails userDetails,
	        @RequestParam(required = false) Boolean all,
	        @PageableDefault(size = 25) Pageable pageable) {

	    String username = userDetails.getUsername();

	    if (Boolean.TRUE.equals(all)) {
	        // Retorna todas as mídias (sem paginação)
	        List<MidiaResponseDto> midias = midiaService.listarMidiasDoUsuario(username);
	        return ResponseEntity.ok(midias);
	    } else {
	        // Retorna mídia paginada
	        Page<MidiaResponseDto> midiasPaginadas = midiaService.listarMidiasDoUsuarioPaginadas(username, pageable);
	        return ResponseEntity.ok(midiasPaginadas);
	    }
	}


	@GetMapping("/usuario/paginado")
	public ResponseEntity<Page<MidiaResponseDto>> listarMidiasPaginadasDoUsuario(
	        @AuthenticationPrincipal UserDetails userDetails,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "25") int size
	) {
	    String username = userDetails.getUsername();
	    Pageable pageable = PageRequest.of(page, size, Sort.by("tituloAlternativo").ascending());
	    Page<MidiaResponseDto> midias = midiaService.listarMidiasPaginadas(username, pageable);

	    return ResponseEntity.ok(midias);
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<List<MidiaResponseDto>> buscarMidias(
	        @RequestParam String query,
	        Authentication authentication) {
	    String username = authentication.getName();
	    List<MidiaResponseDto> resultados = midiaService.buscarPorTitulo(username, query);
	    return ResponseEntity.ok(resultados);
	}

}
