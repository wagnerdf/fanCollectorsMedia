package com.wagnerdf.fancollectorsmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.fancollectorsmedia.model.MidiaTipo;

public interface MidiaTipoRepository extends JpaRepository<MidiaTipo, Long>{
	boolean existsByNome(String nome);

}
