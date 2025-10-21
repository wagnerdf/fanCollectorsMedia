package com.wagnerdf.fancollectorsmedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.MidiaTipo;

public interface MidiaTipoRepository extends JpaRepository<MidiaTipo, Long>{
	boolean existsByNome(String nome);
	List<MidiaTipo> findByAtivoTrue();
	List<MidiaTipo> findByIdIn(List<Long> ids);
	
	@Query("SELECT m.midiaTipoNome, COUNT(m) FROM Midia m WHERE m.cadastro = :cadastro GROUP BY m.midiaTipoNome ORDER BY m.midiaTipoNome ASC")
	List<Object[]> countMidiasByTipo(@Param("cadastro") Cadastro cadastro);

}
