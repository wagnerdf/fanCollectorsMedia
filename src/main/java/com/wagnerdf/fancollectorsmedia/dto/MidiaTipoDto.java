package com.wagnerdf.fancollectorsmedia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MidiaTipoDto {
	
	private Long id;

	@NotBlank(message = "O nome do tipo de mídia é obrigatório")
    private String nome;

	@NotBlank(message = "A descrição do tipo de mídia é obrigatório")
    private String descricao;
	
	private boolean ativo = true;
	
	public MidiaTipoDto(Long id, String nome, String descricao) {
		this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }
	
}
