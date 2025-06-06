package com.wagnerdf.fancollectorsmedia.dto;

import java.time.LocalDate;
import java.util.List;

import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CadastroDto {

    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotBlank(message = "O sobrenome é obrigatório.")
    private String sobreNome;

    @Past(message = "A data de nascimento deve estar no passado.")
    private LocalDate dataNascimento;

    private String sexo;

    private EnderecoDto endereco; // Pode ser null em algumas requisições

    private String telefone;

    @Email(message = "E-mail inválido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    private String email;

    private String avatarUrl;

    private StatusUsuario status;

    private List<Long> hobbiesIds; // Representa os hobbies por ID, para facilitar em requests
}
