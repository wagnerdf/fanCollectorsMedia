package com.wagnerdf.fancollectorsmedia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.fancollectorsmedia.model.Papel;

public interface PapelRepository extends JpaRepository<Papel, Long>{
	Optional<Papel> findByNome(String nome);

}
