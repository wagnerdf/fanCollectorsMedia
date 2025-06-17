package com.wagnerdf.fancollectorsmedia.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RegisterRequestDto {
	private String nome;
    private String sobreNome;
    private LocalDate dataNascimento;
    private String sexo;
    private String telefone;
    private String login; // email
    private String senha;
    private EnderecoDto endereco;
}
