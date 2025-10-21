package com.wagnerdf.fancollectorsmedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wagnerdf.fancollectorsmedia.dto.MidiaGeneroComTotalDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaTipoComTotalDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.MidiaTipo;

public interface MidiaTipoRepository extends JpaRepository<MidiaTipo, Long>{
	boolean existsByNome(String nome);
	List<MidiaTipo> findByAtivoTrue();
	List<MidiaTipo> findByIdIn(List<Long> ids);
	
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaTipoComTotalDto(m.midiaTipoNome, COUNT(m)) " +
		       "FROM Midia m WHERE m.cadastro = :cadastro GROUP BY m.midiaTipoNome ORDER BY m.midiaTipoNome ASC")
		List<MidiaTipoComTotalDto> countMidiasByTipo(@Param("cadastro") Cadastro cadastro);
	
	// 🔹 Método para retornar os gêneros com total direto do banco
    @Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaGeneroComTotalDto(g, COUNT(m)) " +
           "FROM Midia m JOIN m.generos g " + 
           "WHERE m.cadastro = :cadastro " +
           "GROUP BY g " +
           "ORDER BY g ASC")
    List<MidiaGeneroComTotalDto> countGenerosByCadastro(@Param("cadastro") Cadastro cadastro);


}
