package com.wagnerdf.fancollectorsmedia.dto;

public class MidiaGeneroComTotalDto {
	
	private String genero;
    private Long total;
    
    public MidiaGeneroComTotalDto(String genero, Long total) {
        this.genero = genero;
        this.total = total;
    }

    // getters
    public String getGenero() { return genero; }
    public Long getTotal() { return total; }

}
