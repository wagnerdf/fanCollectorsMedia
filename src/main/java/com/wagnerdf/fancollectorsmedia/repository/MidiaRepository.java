package com.wagnerdf.fancollectorsmedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Midia;

@Repository
public interface MidiaRepository extends JpaRepository<Midia, Long> {
	List<Midia> findByCadastro(Cadastro cadastro);
}

