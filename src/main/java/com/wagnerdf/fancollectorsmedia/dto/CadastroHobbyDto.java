package com.wagnerdf.fancollectorsmedia.dto;

import java.time.LocalDateTime;

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

    private Integer nivelInteresse;
}
