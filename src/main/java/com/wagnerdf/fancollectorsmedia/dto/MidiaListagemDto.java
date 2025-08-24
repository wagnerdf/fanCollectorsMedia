package com.wagnerdf.fancollectorsmedia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MidiaListagemDto {
    private String capaUrl;
    private String midiaTipoNome;
    private String generos;
    private String tituloAlternativo;

    public MidiaListagemDto(String capaUrl, String midiaTipoNome, String generos, String tituloAlternativo) {
        this.capaUrl = capaUrl;
        this.midiaTipoNome = midiaTipoNome;
        this.generos = generos;
        this.tituloAlternativo = tituloAlternativo;
    }
}
