package com.wagnerdf.fancollectorsmedia.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.wagnerdf.fancollectorsmedia.model.enums.EstadoConservacao;

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
    private String regiao;
    private String faixas;
    private String classificacaoEtaria;
    private String artistas;
    private String diretores;
    private String estudio;
    private Boolean midiaDigitalInclusa;
    private String formatoAudio;
    private String formatoVideo;
    private String observacoes;
    private Integer quantidadeItens;
    private EstadoConservacao estadoConservacao;
    private Integer anoLancamento;
    private LocalDate adquiridoEm;
    private BigDecimal valorPago;
    private String capaUrl;
    private String tipoMidia;
    private String sinopse;
    private String generos;
    private Integer duracao;
    private String linguagem;
    private Double notaMedia;

    // Getters e Setters
}
