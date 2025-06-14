package com.wagnerdf.fancollectorsmedia.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CadastroHobbyDto {

    private Long id;

    private Long cadastroId;

    private Long hobbyId;

    private LocalDateTime dataRegistro;
    
    @NotNull(message = "O nível de interesse é obrigatório.")
    @Min(value = 1, message = "O nível de interesse deve ser no mínimo 1.")
    @Max(value = 5, message = "O nível de interesse deve ser no máximo 5.")
    private Integer nivelInteresse;
    
    private String nomeHobby;
    private String descricaoHobby;
}
