package com.wagnerdf.fancollectorsmedia.dto;

import lombok.Data;

@Data
public class MidiaBuscaDto {
    private Long id;
    private String tituloOriginal;
    private String tituloAlternativo;
    private String midiaTipo;

    public MidiaBuscaDto(Long id, String tituloOriginal, String tituloAlternativo, String midiaTipo) {
        this.id = id;
        this.tituloOriginal = tituloOriginal;
        this.tituloAlternativo = tituloAlternativo;
        this.midiaTipo = midiaTipo;
    }
}

