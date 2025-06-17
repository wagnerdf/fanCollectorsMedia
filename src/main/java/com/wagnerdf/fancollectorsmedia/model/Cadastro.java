package com.wagnerdf.fancollectorsmedia.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@ToString
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

	@NotBlank(message = "O nome é obrigatório.")
	private String nome;

	@NotBlank(message = "O sobrenome é obrigatório.")
	private String sobreNome;

	@Past(message = "A data de nascimento deve estar no passado.")
	private LocalDate dataNascimento;

	private String sexo;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
	@JoinColumn(name = "endereco_id", referencedColumnName = "id")
	private Endereco endereco;

	private String telefone;

	@Email(message = "E-mail inválido.")
    @NotBlank(message = "O e-mail é obrigatório.")
	@Column(unique = true)
	private String email;

	private LocalDateTime dataCadastro;

	private String avatarUrl;
	
	@Transient // ← Essa anotação JPA impede que a propriedade seja persistida no banco
	private String senha;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusUsuario status;

	@OneToMany(mappedBy = "cadastro")
	private List<CadastroHobby> hobbies = new ArrayList<>();
}
