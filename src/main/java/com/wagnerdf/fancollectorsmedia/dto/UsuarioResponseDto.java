package com.wagnerdf.fancollectorsmedia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioResponseDto {

    private Long id;

    private String login;

    private String nomeCadastro; // ou outro dado de Cadastro que queira mostrar

    private String papelNome;
}
