package com.wagnerdf.fancollectorsmedia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "midia_tipo")
@Getter
@Setter
public class MidiaTipo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O nome do tipo de mídia é obrigatório")
	@Column(nullable = false, unique = true, length = 100)
	private String nome;
	
	@NotBlank(message = "A descrição do tipo de mídia é obrigatório")
	@Column(columnDefinition = "TEXT")
	private String descricao;
	
	@Column(nullable = false)
	private boolean ativo = true;

}
