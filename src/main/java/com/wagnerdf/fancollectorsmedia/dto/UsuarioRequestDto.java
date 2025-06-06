package com.wagnerdf.fancollectorsmedia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioRequestDto {

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Formato de e-mail inválido")
    private String login;

    @NotBlank(message = "Senha não pode ser vazio")
    private String senha;

    private Long cadastroId;  // para associar o cadastro existente

    private Long papelId;     // para associar o papel
}
