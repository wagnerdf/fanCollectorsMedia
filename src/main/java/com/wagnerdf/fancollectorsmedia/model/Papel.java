package com.wagnerdf.fancollectorsmedia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
//@AllArgsConstructor
public class Papel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome; // EX: "ROLE_USER", "ROLE_ADMIN"
	
	public Papel(Long id, String nome) {
	    this.id = id;
	    this.nome = nome;
	}
}
