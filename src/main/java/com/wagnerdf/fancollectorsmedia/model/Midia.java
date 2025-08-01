package com.wagnerdf.fancollectorsmedia.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

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
    private String faixas;
    private String classificacaoEtaria;
    
    @Column(length = 500)
    private String artistas;
    
    @Column(length = 500)
    private String diretores;
    private String estudio;
    private String formatoAudio;
    private String formatoVideo;
    
    @Column(length = 1000)
    private String observacoes;
    private Integer quantidadeItens;
    
    @Column(length = 2000)
    private String sinopse;
    
    @Column(length = 500)
    private String generos;
    private Integer duracao;
    private String linguagem;
    private Double notaMedia;
    private String formatoMidia;
    private String temporada;
    private Integer anoLancamento;
    private String capaUrl;

    @ManyToOne
    @JoinColumn(name = "midia_tipo_id", nullable = false)
    private MidiaTipo midiaTipo;

    @ManyToOne
    @JoinColumn(name = "cadastro_id", nullable = false)
    private Cadastro cadastro;

    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = null; // Deixa null na criação
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

}

