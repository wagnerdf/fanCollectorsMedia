package com.wagnerdf.fancollectorsmedia.dto;

public class MidiaTipoComTotalDto {
	
	private String tipo;
    private Long total;

    public MidiaTipoComTotalDto(String tipo, Long total) {
        this.tipo = tipo;
        this.total = total;
    }

    // getters
    public String getTipo() { return tipo; }
    public Long getTotal() { return total; }

}
