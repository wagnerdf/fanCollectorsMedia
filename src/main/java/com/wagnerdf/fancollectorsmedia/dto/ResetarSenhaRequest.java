package com.wagnerdf.fancollectorsmedia.dto;

import lombok.Data;

@Data
public class ResetarSenhaRequest {

	private String token;
    private String novaSenha;
}
