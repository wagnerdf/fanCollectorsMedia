package com.wagnerdf.fancollectorsmedia.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MidiaResponseDto {

    private Long id;
    private String tituloOriginal;
    private String tituloAlternativo;
    private String edicao;
    private String colecao;
    private String numeroSerie;
    private String faixas;
    private String classificacaoEtaria;
    private String artistas;
    private String diretores;
    private String estudio;
    private String formatoAudio;
    private String formatoVideo;
    private String observacoes;
    private Integer quantidadeItens;
    private String sinopse;
    private String generos;
    private Integer duracao;
    private String linguagem;
    private Double notaMedia;
    private String formatoMidia;
    private String temporada;
    private Integer anoLancamento;
    private String capaUrl;
    private Long midiaTipoId;
    private String tipoMidia;

    public MidiaResponseDto(Long id, String tituloOriginal, String tituloAlternativo, String edicao, String colecao,
            String numeroSerie, String faixas, String classificacaoEtaria, String artistas, String diretores,
            String estudio, String formatoAudio, String formatoVideo, String observacoes, Integer quantidadeItens,
            Integer anoLancamento, String capaUrl, String sinopse, String generos, Integer duracao, String linguagem,
            Double notaMedia, String formatoMidia, String temporada, String midiaTipoNome, Long midiaTipoId) {
        this.id = id;
        this.tituloOriginal = tituloOriginal;
        this.tituloAlternativo = tituloAlternativo;
        this.edicao = edicao;
        this.colecao = colecao;
        this.numeroSerie = numeroSerie;
        this.faixas = faixas;
        this.classificacaoEtaria = classificacaoEtaria;
        this.artistas = artistas;
        this.diretores = diretores;
        this.estudio = estudio;
        this.formatoAudio = formatoAudio;
        this.formatoVideo = formatoVideo;
        this.observacoes = observacoes;
        this.quantidadeItens = quantidadeItens;
        this.anoLancamento = anoLancamento;
        this.capaUrl = capaUrl;
        this.sinopse = sinopse;
        this.generos = generos;
        this.duracao = duracao;
        this.linguagem = linguagem;
        this.notaMedia = notaMedia;
        this.formatoMidia = formatoMidia;
        this.temporada = temporada;
        this.tipoMidia = midiaTipoNome;
        this.midiaTipoId = midiaTipoId;
    }
}

