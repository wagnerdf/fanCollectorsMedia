package com.wagnerdf.fancollectorsmedia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MidiaTipoDto {

	@NotBlank(message = "O nome do tipo de mídia é obrigatório")
    private String nome;

	@NotBlank(message = "A descrição do tipo de mídia é obrigatório")
    private String descricao;
	
}
