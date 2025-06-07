package com.wagnerdf.fancollectorsmedia.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroRequestDto {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O sobrenome é obrigatório.")
    private String sobreNome;

    @Past(message = "A data de nascimento deve estar no passado.")
    private LocalDate dataNascimento;

    private String sexo;

    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    private String email;
    
    private String avatarUrl;

    @NotBlank(message = "A senha é obrigatória.")
    private String senha;

    @Valid
    private EnderecoDto endereco;

    @Valid
    private List<CadastroHobbyDto> hobbies;
}


