package com.wagnerdf.fancollectorsmedia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HobbyDto {

    private Long id;

    @NotBlank(message = "O nome do hobby é obrigatório.")
    private String nome;

    private String descricao;
}
