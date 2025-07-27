package com.wagnerdf.fancollectorsmedia.model;

import com.wagnerdf.fancollectorsmedia.model.enums.EstadoConservacao;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "midia")
@Data
public class Midia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String sinopse;
    private String generos;
    private Integer duracao;
    private String linguagem;
    private Double notaMedia;
    private String formatoMidia;

    @Enumerated(EnumType.STRING)
    private EstadoConservacao estadoConservacao;

    private Integer anoLancamento;
    private LocalDate adquiridoEm;
    private BigDecimal valorPago;
    private String capaUrl;

    @ManyToOne
    @JoinColumn(name = "midia_tipo_id", nullable = false)
    private MidiaTipo midiaTipo;

    @ManyToOne
    @JoinColumn(name = "cadastro_id", nullable = false)
    private Cadastro cadastro;

    private LocalDateTime criadoEm = LocalDateTime.now();
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}

