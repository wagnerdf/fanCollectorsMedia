package com.wagnerdf.fancollectorsmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MidiaListagemMobileDto {
	private Long id;
	private String capaUrl;
	private String midiaTipoNome;
	private String generos;
	private String tituloAlternativo;
	private Double notaMedia;
}
