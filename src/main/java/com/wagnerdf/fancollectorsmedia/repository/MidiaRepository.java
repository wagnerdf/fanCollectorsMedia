package com.wagnerdf.fancollectorsmedia.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wagnerdf.fancollectorsmedia.dto.MidiaBuscaDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Midia;

@Repository
public interface MidiaRepository extends JpaRepository<Midia, Long> {
	List<Midia> findByCadastro(Cadastro cadastro);
	Page<Midia> findByCadastroIdOrderByTituloAlternativoAsc(Long cadastroId, Pageable pageable);
	Page<Midia> findByCadastroEmail(String email, Pageable pageable);
	
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaBuscaDto(" +
		       "m.id, m.tituloOriginal, m.tituloAlternativo, m.midiaTipo.nome) " +
		       "FROM Midia m " +
		       "WHERE m.cadastro.email = :username " +  // <-- trocar username para email, por exemplo
		       "AND (LOWER(m.tituloOriginal) LIKE LOWER(CONCAT('%', :query, '%')) " +
		       "OR LOWER(m.tituloAlternativo) LIKE LOWER(CONCAT('%', :query, '%')))")
		List<MidiaBuscaDto> buscarPorTitulo(@Param("username") String username, @Param("query") String query);






}

