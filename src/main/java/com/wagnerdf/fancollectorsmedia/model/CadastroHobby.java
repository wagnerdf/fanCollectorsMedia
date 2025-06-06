package com.wagnerdf.fancollectorsmedia.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cadastro_hobby")
public class CadastroHobby implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cadastro_id", nullable = false)
	private Cadastro cadastro;

	@ManyToOne
	@JoinColumn(name = "hobby_id", nullable = true)
	private Hobby hobby;

	private LocalDateTime dataRegistro = LocalDateTime.now();

	private Integer nivelInteresse; // Exemplo: 1 a 5

	public CadastroHobby() {
	}

	public CadastroHobby(Cadastro cadastro, Hobby hobby) {
		this.cadastro = cadastro;
		this.hobby = hobby;
	}

	public Long getId() {
		return id;
	}

	public Cadastro getCadastro() {
		return cadastro;
	}

	public void setCadastro(Cadastro cadastro) {
		this.cadastro = cadastro;
	}

	public Hobby getHobby() {
		return hobby;
	}

	public void setHobby(Hobby hobby) {
		this.hobby = hobby;
	}

	public LocalDateTime getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(LocalDateTime dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Integer getNivelInteresse() {
		return nivelInteresse;
	}

	public void setNivelInteresse(Integer nivelInteresse) {
		this.nivelInteresse = nivelInteresse;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CadastroHobby other = (CadastroHobby) obj;
		return Objects.equals(id, other.id);
	}

}