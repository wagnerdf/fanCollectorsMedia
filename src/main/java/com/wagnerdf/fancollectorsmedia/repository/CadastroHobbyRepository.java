package com.wagnerdf.fancollectorsmedia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.CadastroHobby;

public interface CadastroHobbyRepository extends JpaRepository<CadastroHobby, Long>{
	
	List<CadastroHobby> findByCadastro(Cadastro cadastro);

}
