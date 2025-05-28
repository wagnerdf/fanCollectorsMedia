package com.wagnerdf.fancollectorsmedia.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String nome;
    private String email;
    private String senha;
}
