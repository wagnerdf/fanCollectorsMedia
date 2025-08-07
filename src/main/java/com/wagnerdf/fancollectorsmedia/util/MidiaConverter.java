package com.wagnerdf.fancollectorsmedia.util;

import com.wagnerdf.fancollectorsmedia.dto.MidiaResponseDto;
import com.wagnerdf.fancollectorsmedia.model.Midia;

public class MidiaConverter {

    public static MidiaResponseDto toDto(Midia midia) {
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
        dto.setTipoMidia(midia.getMidiaTipo().getNome());
        dto.setSinopse(midia.getSinopse());
        dto.setGeneros(midia.getGeneros());
        dto.setDuracao(midia.getDuracao());
        dto.setLinguagem(midia.getLinguagem());
        dto.setNotaMedia(midia.getNotaMedia());
        dto.setFormatoMidia(midia.getFormatoMidia());
        dto.setTemporada(midia.getTemporada());

        return dto;
    }
}



