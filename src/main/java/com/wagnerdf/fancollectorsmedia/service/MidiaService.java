package com.wagnerdf.fancollectorsmedia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		midia.setRegiao(dto.getRegiao());
		midia.setFaixas(dto.getFaixas());
		midia.setClassificacaoEtaria(dto.getClassificacaoEtaria());
		midia.setArtistas(dto.getArtistas());
		midia.setDiretores(dto.getDiretores());
		midia.setEstudio(dto.getEstudio());
		midia.setMidiaDigitalInclusa(dto.getMidiaDigitalInclusa());
		midia.setFormatoAudio(dto.getFormatoAudio());
		midia.setFormatoVideo(dto.getFormatoVideo());
		midia.setObservacoes(dto.getObservacoes());
		midia.setQuantidadeItens(dto.getQuantidadeItens());
		midia.setEstadoConservacao(dto.getEstadoConservacao());
		midia.setAnoLancamento(dto.getAnoLancamento());
		midia.setAdquiridoEm(dto.getAdquiridoEm());
		midia.setValorPago(dto.getValorPago());
		midia.setCapaUrl(dto.getCapaUrl());
	    midia.setSinopse(dto.getSinopse());
	    midia.setGeneros(dto.getGeneros());
	    midia.setDuracao(dto.getDuracao());
	    midia.setLinguagem(dto.getLinguagem());
	    midia.setNotaMedia(dto.getNotaMedia());
	    midia.setFormatoMidia(dto.getFormatoMidia());
	    midia.setTemporada(dto.getTemporada());
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

	public List<MidiaResponseDto> listarMidiasDoUsuario(String username) {
		Cadastro cadastro = cadastroService.buscarPorUsername(username);
		return midiaRepository.findByCadastro(cadastro).stream().map(this::toDto).toList();
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
		dto.setRegiao(midia.getRegiao());
		dto.setFaixas(midia.getFaixas());
		dto.setClassificacaoEtaria(midia.getClassificacaoEtaria());
		dto.setArtistas(midia.getArtistas());
		dto.setDiretores(midia.getDiretores());
		dto.setEstudio(midia.getEstudio());
		dto.setMidiaDigitalInclusa(midia.getMidiaDigitalInclusa());
		dto.setFormatoAudio(midia.getFormatoAudio());
		dto.setFormatoVideo(midia.getFormatoVideo());
		dto.setObservacoes(midia.getObservacoes());
		dto.setQuantidadeItens(midia.getQuantidadeItens());
		dto.setEstadoConservacao(midia.getEstadoConservacao());
		dto.setAnoLancamento(midia.getAnoLancamento());
		dto.setAdquiridoEm(midia.getAdquiridoEm());
		dto.setValorPago(midia.getValorPago());
		dto.setCapaUrl(midia.getCapaUrl());
		dto.setSinopse(midia.getSinopse());
		dto.setGeneros(midia.getGeneros());
		dto.setDuracao(midia.getDuracao());
		dto.setLinguagem(midia.getLinguagem());
		dto.setNotaMedia(midia.getNotaMedia());
		dto.setFormatoMidia(midia.getFormatoMidia());
		dto.setTemporada(midia.getTemporada());
		dto.setTipoMidia(midia.getMidiaTipo().getNome());

		return dto;
	}
	
	public Midia editarMidia(Long id, MidiaRequestDto dto, String username) {
	    Midia midia = midiaRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Mídia não encontrada"));

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
	    midia.setRegiao(dto.getRegiao());
	    midia.setFaixas(dto.getFaixas());
	    midia.setClassificacaoEtaria(dto.getClassificacaoEtaria());
	    midia.setArtistas(dto.getArtistas());
	    midia.setDiretores(dto.getDiretores());
	    midia.setEstudio(dto.getEstudio());
	    midia.setMidiaDigitalInclusa(dto.getMidiaDigitalInclusa());
	    midia.setFormatoAudio(dto.getFormatoAudio());
	    midia.setFormatoVideo(dto.getFormatoVideo());
	    midia.setObservacoes(dto.getObservacoes());
	    midia.setQuantidadeItens(dto.getQuantidadeItens());
	    midia.setEstadoConservacao(dto.getEstadoConservacao());
	    midia.setAnoLancamento(dto.getAnoLancamento());
	    midia.setAdquiridoEm(dto.getAdquiridoEm());
	    midia.setValorPago(dto.getValorPago());
	    midia.setCapaUrl(dto.getCapaUrl());
	    midia.setSinopse(dto.getSinopse());
	    midia.setGeneros(dto.getGeneros());
	    midia.setDuracao(dto.getDuracao());
	    midia.setLinguagem(dto.getLinguagem());
	    midia.setNotaMedia(dto.getNotaMedia());
	    midia.setFormatoMidia(dto.getFormatoMidia());
	    midia.setTemporada(dto.getTemporada());
	    midia.setMidiaTipo(midiaTipo);

	    return midiaRepository.save(midia);
	}


}
