package com.wagnerdf.fancollectorsmedia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.dto.MidiaCamposLivresDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaListagemDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaListagemMobileDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaRequestDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaResponseDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Midia;
import com.wagnerdf.fancollectorsmedia.model.MidiaTipo;
import com.wagnerdf.fancollectorsmedia.repository.MidiaRepository;

@Service
public class MidiaService {

	@Autowired
	private MidiaRepository midiaRepository;

	@Autowired
	private CadastroService cadastroService;

	@Autowired
	private MidiaTipoService midiaTipoService;

	// Agora recebe username do usuário logado para buscar o Cadastro
	public Midia salvarMidia(MidiaRequestDto dto, String username) {

		Cadastro cadastro = cadastroService.buscarPorUsername(username);
		MidiaTipo midiaTipo = midiaTipoService.buscarPorId(dto.getMidiaTipoId());

		Midia midia = new Midia();

		midia.setTituloOriginal(dto.getTituloOriginal());
		midia.setTituloAlternativo(dto.getTituloAlternativo());
		midia.setEdicao(dto.getEdicao());
		midia.setColecao(dto.getColecao());
		midia.setNumeroSerie(dto.getNumeroSerie());
		midia.setFaixas(dto.getFaixas());
		midia.setClassificacaoEtaria(dto.getClassificacaoEtaria());
		midia.setArtistas(dto.getArtistas());
		midia.setDiretores(dto.getDiretores());
		midia.setEstudio(dto.getEstudio());
		midia.setFormatoAudio(dto.getFormatoAudio());
		midia.setFormatoVideo(dto.getFormatoVideo());
		midia.setObservacoes(dto.getObservacoes());
		midia.setQuantidadeItens(dto.getQuantidadeItens());
		midia.setAnoLancamento(dto.getAnoLancamento());
		midia.setCapaUrl(dto.getCapaUrl());
		midia.setSinopse(dto.getSinopse());
		midia.setGeneros(dto.getGeneros());
		midia.setDuracao(dto.getDuracao());
		midia.setLinguagem(dto.getLinguagem());
		midia.setNotaMedia(dto.getNotaMedia());
		midia.setFormatoMidia(dto.getFormatoMidia());
		midia.setTemporada(dto.getTemporada());
		midia.setMidiaTipoNome(dto.getMidiaTipoNome());
		midia.setAssistido(dto.isAssistido());
		midia.setCadastro(cadastro);
		midia.setMidiaTipo(midiaTipo);

		return midiaRepository.save(midia);
	}

	public void deletarMidia(Long id, String username) {
		Midia midia = midiaRepository.findById(id).orElseThrow(() -> new RuntimeException("Mídia não encontrada"));

		// Verifica se a mídia pertence ao usuário logado
		if (!midia.getCadastro().getEmail().equals(username)) {
			throw new RuntimeException("Você não tem permissão para deletar esta mídia.");
		}

		midiaRepository.delete(midia);
	}

	// Lista todas as mídias de um usuário (sem paginação)
	public List<MidiaListagemDto> listarMidiasDoUsuario(String username) {
		Cadastro cadastro = cadastroService.buscarPorUsername(username);
		return midiaRepository.findByCadastroDto(cadastro);
	}

	public MidiaResponseDto buscarMidiaPorId(Long id, String username) {
		Midia midia = midiaRepository.findById(id).orElseThrow(() -> new RuntimeException("Mídia não encontrada"));

		if (!midia.getCadastro().getEmail().equals(username)) {
			throw new RuntimeException("Você não tem permissão para visualizar esta mídia.");
		}

		return toDto(midia);
	}

	private MidiaResponseDto toDto(Midia midia) {
		MidiaResponseDto dto = new MidiaResponseDto();

		dto.setId(midia.getId());
		dto.setTituloOriginal(midia.getTituloOriginal());
		dto.setTituloAlternativo(midia.getTituloAlternativo());
		dto.setEdicao(midia.getEdicao());
		dto.setColecao(midia.getColecao());
		dto.setNumeroSerie(midia.getNumeroSerie());
		dto.setFaixas(midia.getFaixas());
		dto.setClassificacaoEtaria(midia.getClassificacaoEtaria());
		dto.setArtistas(midia.getArtistas());
		dto.setDiretores(midia.getDiretores());
		dto.setEstudio(midia.getEstudio());
		dto.setFormatoAudio(midia.getFormatoAudio());
		dto.setFormatoVideo(midia.getFormatoVideo());
		dto.setObservacoes(midia.getObservacoes());
		dto.setQuantidadeItens(midia.getQuantidadeItens());
		dto.setAnoLancamento(midia.getAnoLancamento());
		dto.setCapaUrl(midia.getCapaUrl());
		dto.setSinopse(midia.getSinopse());
		dto.setGeneros(midia.getGeneros());
		dto.setDuracao(midia.getDuracao());
		dto.setLinguagem(midia.getLinguagem());
		dto.setNotaMedia(midia.getNotaMedia());
		dto.setFormatoMidia(midia.getFormatoMidia());
		dto.setTemporada(midia.getTemporada());
		dto.setMidiaTipoId(midia.getMidiaTipo().getId());
		dto.setMidiaTipoNome(midia.getMidiaTipo().getNome());
		dto.setAssistido(midia.isAssistido());

		return dto;
	}

	public Midia editarMidia(Long id, MidiaRequestDto dto, String username) {
		Midia midia = midiaRepository.findById(id).orElseThrow(() -> new RuntimeException("Mídia não encontrada"));

		if (!midia.getCadastro().getEmail().equals(username)) {
			throw new RuntimeException("Você não tem permissão para editar esta mídia.");
		}

		MidiaTipo midiaTipo = midiaTipoService.buscarPorId(dto.getMidiaTipoId());

		// Atualiza os campos
		midia.setTituloOriginal(dto.getTituloOriginal());
		midia.setTituloAlternativo(dto.getTituloAlternativo());
		midia.setEdicao(dto.getEdicao());
		midia.setColecao(dto.getColecao());
		midia.setNumeroSerie(dto.getNumeroSerie());
		midia.setFaixas(dto.getFaixas());
		midia.setClassificacaoEtaria(dto.getClassificacaoEtaria());
		midia.setArtistas(dto.getArtistas());
		midia.setDiretores(dto.getDiretores());
		midia.setEstudio(dto.getEstudio());
		midia.setFormatoAudio(dto.getFormatoAudio());
		midia.setFormatoVideo(dto.getFormatoVideo());
		midia.setObservacoes(dto.getObservacoes());
		midia.setQuantidadeItens(dto.getQuantidadeItens());
		midia.setAnoLancamento(dto.getAnoLancamento());
		midia.setCapaUrl(dto.getCapaUrl());
		midia.setSinopse(dto.getSinopse());
		midia.setGeneros(dto.getGeneros());
		midia.setDuracao(dto.getDuracao());
		midia.setLinguagem(dto.getLinguagem());
		midia.setNotaMedia(dto.getNotaMedia());
		midia.setFormatoMidia(dto.getFormatoMidia());
		midia.setTemporada(dto.getTemporada());
		midia.setMidiaTipoNome(dto.getMidiaTipoNome());
		midia.setAssistido(dto.isAssistido());
		midia.setMidiaTipo(midiaTipo);

		return midiaRepository.save(midia);
	}

	public Page<MidiaListagemDto> listarMidiasPaginadas(String email, Pageable pageable) {
		return midiaRepository.findByCadastroEmailDto(email, pageable);
	}

	// Lista mídias paginadas de um usuário
	public Page<MidiaListagemDto> listarMidiasDoUsuarioPaginadas(String username, Pageable pageable) {
		return midiaRepository.findByCadastroEmailDto(username, pageable);
	}

	public List<MidiaResponseDto> buscarPorTitulo(String username, String query) {
		return midiaRepository.buscarPorTitulo(username, query);
	}

	public Midia editarCamposLivres(Long id, MidiaCamposLivresDto dto, String username) {
		Midia midia = midiaRepository.findById(id).orElseThrow(() -> new RuntimeException("Mídia não encontrada"));

		if (!midia.getCadastro().getEmail().equals(username)) {
			throw new RuntimeException("Você não tem permissão para editar esta mídia.");
		}

		// Atualiza somente se o campo não for nulo
		if (dto.getObservacoes() != null) {
			midia.setObservacoes(dto.getObservacoes());
		}
		if (dto.getTemporada() != null) {
			midia.setTemporada(dto.getTemporada());
		}
		if (dto.getMidiaTipoNome() != null) {
			midia.setMidiaTipoNome(dto.getMidiaTipoNome());
		}
		if (dto.getMidiaTipoId() != null) {
			MidiaTipo tipo = new MidiaTipo();
			tipo.setId(dto.getMidiaTipoId());
			midia.setMidiaTipo(tipo);
		}

		return midiaRepository.save(midia);
	}

	// Retorna mídias paginadas (todos os tipos)
	public Page<MidiaRequestDto> listarTiposPaginados(Pageable pageable) {
		return midiaRepository.findAll(pageable).map(m -> {
			MidiaRequestDto dto = new MidiaRequestDto();
			dto.setCapaUrl(m.getCapaUrl());
			dto.setMidiaTipoNome(m.getMidiaTipoNome());
			dto.setGeneros(m.getGeneros());
			dto.setTituloAlternativo(m.getTituloAlternativo());
			return dto;
		});
	}

	// versões com paginação de midias do usuario e tipoMidia
	public Page<MidiaListagemDto> listarPorTiposDoUsuario(String email, List<String> tipos, Pageable pageable) {
		return midiaRepository.findByTiposAndUsuario(email, tipos, pageable);
	}

	public Page<MidiaListagemDto> listarTodosDoUsuario(String email, Pageable pageable) {
		return midiaRepository.findByTiposAndUsuario(email, null, pageable);
	}

	// versões sem paginação de midias do usuario e tipoMidia
	public List<MidiaListagemDto> listarPorTiposDoUsuarioSemPaginacao(String email, List<String> tipos) {
		// Chama o repository equivalente sem Pageable
		return midiaRepository.findByTiposAndUsuarioSemPaginacao(email, tipos);
	}

	public List<MidiaListagemDto> listarTodosDoUsuarioSemPaginacao(String email) {
		// Chama o repository equivalente sem Pageable
		return midiaRepository.findByTiposAndUsuarioSemPaginacao(email, null);
	}

	public long contarMidiasDoUsuario(String email) {
		return midiaRepository.countByCadastroEmail(email);
	}

	public Map<String, Object> listarMidiasDoUsuario(String email, int offset, int limit) {
		Cadastro cadastro = cadastroService.buscarPorEmail(email);

		List<Object[]> resultados = midiaRepository.findMidiasListagemByCadastro(cadastro.getId(), limit, offset);
		long total = midiaRepository.countMidiasByCadastro(cadastro.getId());

		boolean hasMore = offset + limit < total;

		List<MidiaListagemMobileDto> midias = resultados.stream()
				.map(r -> new MidiaListagemMobileDto(((Number) r[0]).longValue(), (String) r[1], (String) r[2],
						(String) r[3], (String) r[4], r[5] != null ? ((Number) r[5]).doubleValue() : null))
				.toList();

		Map<String, Object> response = new HashMap<>();
		response.put("total", total);
		response.put("midias", midias);
		response.put("hasMore", hasMore);

		return response;
	}
	
	public Page<MidiaListagemMobileDto> buscarPorUsuarioEGeneroIgnoreCase(
	        Long cadastroId, String nomeGenero, int page, int size) {

	    Pageable pageable = PageRequest.of(
	        Math.max(0, page), 
	        Math.max(1, size)
	    );

	    return midiaRepository.buscarPorUsuarioEGeneroIgnoreCase(cadastroId, nomeGenero, pageable);
	}
	
	public Page<MidiaListagemMobileDto> buscarPorUsuarioETipoMidiaIgnoreCase(
	        Long cadastroId, String tipoMidia, int page, int size) {

	    Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));
	    return midiaRepository.buscarPorUsuarioETipoMidiaIgnoreCase(cadastroId, tipoMidia, pageable);
	}


}