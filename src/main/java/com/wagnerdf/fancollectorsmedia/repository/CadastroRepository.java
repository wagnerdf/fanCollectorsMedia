package com.wagnerdf.fancollectorsmedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;

public interface CadastroRepository extends JpaRepository<Cadastro, Long>{
	List<Cadastro>findByStatus(StatusUsuario status);
	
	boolean existsByEmail(String email);
	
}
