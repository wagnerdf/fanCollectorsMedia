package com.wagnerdf.fancollectorsmedia.dto;

import com.wagnerdf.fancollectorsmedia.model.enums.EstadoConservacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MidiaRequestDto {

    private String nome;
    private String tituloOriginal;
    private String tituloAlternativo;
    private String edicao;
    private String colecao;
    private String numeroSerie;
    private String regiao;
    private String faixas;
    private String classificacaoEtaria;
    private String artistaDiretor;
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
    private Long midiaTipoId;
    private Long cadastroId;
}
