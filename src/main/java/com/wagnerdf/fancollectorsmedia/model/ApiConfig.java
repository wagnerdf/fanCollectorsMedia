package com.wagnerdf.fancollectorsmedia.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_config")
@Data
public class ApiConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String chave;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String valor;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    // Construtor customizado
    public ApiConfig(String chave, String valor) {
        this.chave = chave;
        this.valor = valor;
        this.atualizadoEm = LocalDateTime.now();
    }

    // Construtor padrão para JPA
    public ApiConfig() {}
}
