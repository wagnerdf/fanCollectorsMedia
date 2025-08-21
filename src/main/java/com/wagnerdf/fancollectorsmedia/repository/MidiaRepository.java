package com.wagnerdf.fancollectorsmedia.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wagnerdf.fancollectorsmedia.dto.MidiaResponseDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Midia;

@Repository
public interface MidiaRepository extends JpaRepository<Midia, Long> {
	List<Midia> findByCadastro(Cadastro cadastro);
	Page<Midia> findByCadastroIdOrderByTituloAlternativoAsc(Long cadastroId, Pageable pageable);
	Page<Midia> findByCadastroEmail(String email, Pageable pageable);
	
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaResponseDto(" +
		       "m.id, m.tituloOriginal, m.tituloAlternativo, m.edicao, m.colecao, m.numeroSerie, m.faixas, " +
		       "m.classificacaoEtaria, m.artistas, m.diretores, m.estudio, m.formatoAudio, m.formatoVideo, " +
		       "m.observacoes, m.quantidadeItens, m.anoLancamento, m.capaUrl, m.sinopse, m.generos, m.duracao, " +
		       "m.linguagem, m.notaMedia, m.formatoMidia, m.temporada, m.midiaTipoNome, m.midiaTipo.id) " +
		       "FROM Midia m " +
		       "WHERE m.cadastro.email = :username " +
		       "AND (LOWER(m.tituloOriginal) LIKE LOWER(CONCAT('%', :query, '%')) " +
		       "OR LOWER(m.tituloAlternativo) LIKE LOWER(CONCAT('%', :query, '%')))")
		List<MidiaResponseDto> buscarPorTitulo(@Param("username") String username, @Param("query") String query);

	
}

