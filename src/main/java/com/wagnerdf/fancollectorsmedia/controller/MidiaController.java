package com.wagnerdf.fancollectorsmedia.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.dto.MidiaCamposLivresDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaListagemDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaResponseDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaTipoComTotalDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Midia;
import com.wagnerdf.fancollectorsmedia.model.MidiaTipo;
import com.wagnerdf.fancollectorsmedia.repository.MidiaRepository;
import com.wagnerdf.fancollectorsmedia.repository.MidiaTipoRepository;
import com.wagnerdf.fancollectorsmedia.service.CadastroService;
import com.wagnerdf.fancollectorsmedia.service.MidiaService;
import com.wagnerdf.fancollectorsmedia.service.MidiaTipoService;

@RestController
@RequestMapping("/api/midias")
public class MidiaController {

	@Autowired
	private MidiaService midiaService;

	@Autowired
	private MidiaTipoRepository midiaTipoRepository;

	@Autowired
	private MidiaTipoService midiaTipoService;

	@Autowired
	private CadastroService cadastroService;

	@Autowired
	private MidiaRepository midiaRepository;

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
	public ResponseEntity<List<MidiaListagemDto>> listarTodas(Authentication authentication) {
		String username = authentication.getName();
		List<MidiaListagemDto> midias = midiaService.listarMidiasDoUsuario(username);
		return ResponseEntity.ok(midias);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MidiaResponseDto> buscarPorId(@PathVariable Long id, Authentication authentication) {
		String username = authentication.getName();
		MidiaResponseDto midia = midiaService.buscarMidiaPorId(id, username);
		return ResponseEntity.ok(midia);
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> atualizarMidia(@PathVariable Long id, @RequestBody MidiaRequestDto dto,
			Authentication authentication) {
		String username = authentication.getName();
		midiaService.editarMidia(id, dto, username);
		return ResponseEntity.ok("Mídia atualizada com sucesso");
	}

	@GetMapping("/usuario")
	public ResponseEntity<?> listarMidiasDoUsuario(@AuthenticationPrincipal UserDetails userDetails,
			@RequestParam(required = false) Boolean all, @PageableDefault(size = 25) Pageable pageable) {

		String username = userDetails.getUsername();

		if (Boolean.TRUE.equals(all)) {
			// Retorna todas as mídias (sem paginação)
			List<MidiaListagemDto> midias = midiaService.listarMidiasDoUsuario(username);
			return ResponseEntity.ok(midias);
		} else {
			// Retorna mídia paginada
			Page<MidiaListagemDto> midiasPaginadas = midiaService.listarMidiasDoUsuarioPaginadas(username, pageable);
			return ResponseEntity.ok(midiasPaginadas);
		}
	}

	@GetMapping("/usuario/paginado")
	public ResponseEntity<Page<MidiaListagemDto>> listarMidiasPaginadasDoUsuario(
			@AuthenticationPrincipal UserDetails userDetails, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size) {

		String username = userDetails.getUsername();
		Pageable pageable = PageRequest.of(page, size, Sort.by("tituloAlternativo").ascending());

		Page<MidiaListagemDto> midias = midiaService.listarMidiasPaginadas(username, pageable);

		return ResponseEntity.ok(midias);
	}

	@GetMapping("/buscar")
	public ResponseEntity<List<MidiaResponseDto>> buscarMidias(@RequestParam String query,
			Authentication authentication) {
		String username = authentication.getName();
		List<MidiaResponseDto> resultados = midiaService.buscarPorTitulo(username, query);
		return ResponseEntity.ok(resultados);
	}

	@PatchMapping("/{id}/editar-campos-livres")
	public ResponseEntity<String> editarCamposLivres(@PathVariable Long id, @RequestBody MidiaCamposLivresDto dto,
			Authentication authentication) {
		String username = authentication.getName();
		midiaService.editarCamposLivres(id, dto, username);
		return ResponseEntity.ok("Campos atualizados com sucesso");
	}

	@GetMapping("/selecao")
	public List<MidiaTipo> listarTiposParaSelecao(@RequestParam List<Long> ids) {
		return midiaTipoService.listarPorIds(ids);
	}

	// Lista todas as midias do usuário por tipo de midia e tras páginado
	@GetMapping("/tipos-nomes")
	public ResponseEntity<?> listarMidiasPorTipos(Authentication authentication,
			@RequestParam(required = false) List<String> tipos, @RequestParam(required = false) Boolean all,
			@PageableDefault(size = 25) Pageable pageable) {

		String email = authentication.getName();
		Page<MidiaListagemDto> page;

		if (Boolean.TRUE.equals(all) || tipos == null || tipos.isEmpty()) {
			page = midiaService.listarTodosDoUsuario(email, pageable);
		} else {
			page = midiaService.listarPorTiposDoUsuario(email, tipos, pageable);
		}

		// Converter Page em mapa JSON estável
		Map<String, Object> response = new HashMap<>();
		response.put("content", page.getContent());
		response.put("page", page.getNumber());
		response.put("size", page.getSize());
		response.put("totalElements", page.getTotalElements());
		response.put("totalPages", page.getTotalPages());

		return ResponseEntity.ok(response);
	}

	// Lista todas as midias do usuário por tipo de midia sem páginação
	@GetMapping("/tipos-nomes-lista")
	public ResponseEntity<?> listarMidiasPorTiposSemPaginacao(Authentication authentication,
			@RequestParam(required = false) List<String> tipos, @RequestParam(required = false) Boolean all) {

		String email = authentication.getName();
		List<MidiaListagemDto> lista;

		if (Boolean.TRUE.equals(all) || tipos == null || tipos.isEmpty()) {
			lista = midiaService.listarTodosDoUsuarioSemPaginacao(email);
		} else {
			lista = midiaService.listarPorTiposDoUsuarioSemPaginacao(email, tipos);
		}

		// Retornar direto a lista como JSON
		return ResponseEntity.ok(lista);
	}

	// Retorna o total de mídias do usuário logado
	@GetMapping("/usuario/total")
	public ResponseEntity<Map<String, Object>> totalMidiasDoUsuario(@AuthenticationPrincipal UserDetails userDetails) {
		String email = userDetails.getUsername();
		long total = midiaService.contarMidiasDoUsuario(email);

		Map<String, Object> response = new HashMap<>();
		response.put("usuario", email);
		response.put("totalMidias", total);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/minhas")
	public ResponseEntity<Map<String, Object>> listarMinhasMidias(@RequestParam(defaultValue = "0") int offset,
			@RequestParam(defaultValue = "10") int limit, Authentication authentication) {

		String email = authentication.getName();
		return ResponseEntity.ok(midiaService.listarMidiasDoUsuario(email, offset, limit));
	}

	@GetMapping("/generos")
	public ResponseEntity<Map<String, Long>> listarGeneros(Authentication authentication) {

		// Pega o e-mail do usuário logado via Spring Security
		String email = authentication.getName();

		// Busca o cadastro do usuário pelo email
		Cadastro cadastro = cadastroService.buscarPorEmail(email);

		// Busca todas as mídias do usuário
		List<Midia> midias = midiaRepository.findByCadastro(cadastro);

		Map<String, Long> contagemGeneros = new HashMap<>();

		for (Midia midia : midias) {
			if (midia.getGeneros() != null && !midia.getGeneros().isEmpty()) {
				// Substitui conectivos por vírgula para facilitar split
				String[] generos = midia.getGeneros().replace("&", ",").replace(" e ", ",").split(",");
				for (String genero : generos) {
					String g = genero.trim();
					if (!g.isEmpty()) {
						contagemGeneros.put(g, contagemGeneros.getOrDefault(g, 0L) + 1);
					}
				}
			}
		}

		// 🔹 Ordena alfabeticamente antes de retornar
		Map<String, Long> contagemGenerosOrdenada = contagemGeneros.entrySet().stream()
				.sorted(Map.Entry.comparingByKey()) // ordena pela chave (nome do gênero)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new // mantém a ordem
				));

		return ResponseEntity.ok(contagemGenerosOrdenada);
	}

	@GetMapping("/tipos")
	public ResponseEntity<Map<String, Long>> listarTiposMidia(Authentication authentication) {
		String email = authentication.getName();
		Cadastro cadastro = cadastroService.buscarPorEmail(email);

		List<MidiaTipoComTotalDto> resultados = midiaTipoRepository.countMidiasByTipo(cadastro);

		Map<String, Long> tiposMidia = resultados.stream()
				.collect(Collectors.toMap(MidiaTipoComTotalDto::getTipo, MidiaTipoComTotalDto::getTotal));

		return ResponseEntity.ok(tiposMidia);
	}

}