package com.wagnerdf.fancollectorsmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // necessário para serialização
public class AuthResponseDto {
    private String token;
    private String message; 
}


