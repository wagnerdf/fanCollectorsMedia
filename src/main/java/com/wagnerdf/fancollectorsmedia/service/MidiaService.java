package com.wagnerdf.fancollectorsmedia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.dto.MidiaRequestDto;
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

		midia.setNome(dto.getNome());
		midia.setTituloOriginal(dto.getTituloOriginal());
		midia.setTituloAlternativo(dto.getTituloAlternativo());
		midia.setEdicao(dto.getEdicao());
		midia.setColecao(dto.getColecao());
		midia.setNumeroSerie(dto.getNumeroSerie());
		midia.setRegiao(dto.getRegiao());
		midia.setFaixas(dto.getFaixas());
		midia.setClassificacaoEtaria(dto.getClassificacaoEtaria());
		midia.setArtistaDiretor(dto.getArtistaDiretor());
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
		midia.setCadastro(cadastro);
		midia.setMidiaTipo(midiaTipo);

		return midiaRepository.save(midia);
	}
	
	public void deletarMidia(Long id, String username) {
	    Midia midia = midiaRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Mídia não encontrada"));

	    // Verifica se a mídia pertence ao usuário logado
	    if (!midia.getCadastro().getEmail().equals(username)) {
	        throw new RuntimeException("Você não tem permissão para deletar esta mídia.");
	    }

	    midiaRepository.delete(midia);
	}

}
