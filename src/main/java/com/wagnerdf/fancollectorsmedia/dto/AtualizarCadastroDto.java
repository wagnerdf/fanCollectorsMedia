package com.wagnerdf.fancollectorsmedia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AtualizarCadastroDto {

    private String dataNascimento;
    private String sexo;
    private String telefone;

    // Endereço
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;

    // Nova senha (opcional)
    private String novaSenha;

}
