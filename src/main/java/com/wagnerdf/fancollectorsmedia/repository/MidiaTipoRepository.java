package com.wagnerdf.fancollectorsmedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.fancollectorsmedia.model.MidiaTipo;

public interface MidiaTipoRepository extends JpaRepository<MidiaTipo, Long>{
	boolean existsByNome(String nome);
	List<MidiaTipo> findByAtivoTrue();
	List<MidiaTipo> findByIdIn(List<Long> ids);

}
