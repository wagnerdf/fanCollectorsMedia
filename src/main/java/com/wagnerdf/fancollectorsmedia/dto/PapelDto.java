package com.wagnerdf.fancollectorsmedia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PapelDto {

    private Long id;

    @NotBlank(message = "Nome do papel e obrigatorio")
    private String nome;
}