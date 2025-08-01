package com.wagnerdf.fancollectorsmedia.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MidiaRequestDto {

    private String tituloOriginal;
    private String tituloAlternativo;
    private String edicao;
    private String colecao;
    private String numeroSerie;
    private String regiao;
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
    private Long midiaTipoId;
    private Long cadastroId;
    private String sinopse;
    private String generos;
    private Integer duracao;
    private String linguagem;
    private Double notaMedia;
    private String formatoMidia;
    private String temporada;
}
