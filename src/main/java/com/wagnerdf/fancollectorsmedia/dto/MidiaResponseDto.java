package com.wagnerdf.fancollectorsmedia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
    private Integer anoLancamento;
    private String capaUrl;
    private String tipoMidia;
    private String sinopse;
    private String generos;
    private Integer duracao;
    private String linguagem;
    private Double notaMedia;
    private String formatoMidia;
    private String temporada;

    // Getters e Setters
}
