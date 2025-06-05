package com.wagnerdf.fancollectorsmedia.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "cadastro")
public class Cadastro implements Serializable {
	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nome;

	@NotBlank
	private String sobreNome;

	@Past
	private LocalDate dataNascimento;

	private String sexo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "endereco_id", referencedColumnName = "id")
	private Endereco endereco;

	private String telefone;

	@Email
	@NotBlank
	@Column(unique = true)
	private String email;

	private LocalDateTime dataCadastro;

	private String avatarUrl;

	@Enumerated(EnumType.STRING)
	private StatusUsuario status;

	@OneToMany(mappedBy = "cadastro")
	private List<CadastroHobby> hobbies = new ArrayList<>();
}
