package com.wagnerdf.fancollectorsmedia.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wagnerdf.fancollectorsmedia.dto.MidiaListagemDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaListagemMobileDto;
import com.wagnerdf.fancollectorsmedia.dto.MidiaResponseDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.Midia;

@Repository
public interface MidiaRepository extends JpaRepository<Midia, Long> {
	List<Midia> findByCadastro(Cadastro cadastro);

	Page<Midia> findByCadastroIdOrderByTituloAlternativoAsc(Long cadastroId, Pageable pageable);

	// Page<Midia> findByCadastroEmail(String username, Pageable pageable);

	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaResponseDto("
			+ "m.id, m.tituloOriginal, m.tituloAlternativo, m.edicao, m.colecao, m.numeroSerie, m.faixas, "
			+ "m.classificacaoEtaria, m.artistas, m.diretores, m.estudio, m.formatoAudio, m.formatoVideo, "
			+ "m.observacoes, m.quantidadeItens, m.anoLancamento, m.capaUrl, m.sinopse, m.generos, m.duracao, "
			+ "m.linguagem, m.notaMedia, m.formatoMidia, m.temporada, m.midiaTipo.id, m.midiaTipo.nome, m.assistido) "
			+ "FROM Midia m " + "WHERE m.cadastro.email = :username "
			+ "AND (LOWER(m.tituloOriginal) LIKE LOWER(CONCAT('%', :query, '%')) "
			+ "OR LOWER(m.tituloAlternativo) LIKE LOWER(CONCAT('%', :query, '%')))")
	List<MidiaResponseDto> buscarPorTitulo(@Param("username") String username, @Param("query") String query);

	// Pesquisa por tipos e usuário
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaListagemDto("
			+ "m.id, m.capaUrl, m.midiaTipoNome, m.generos, m.tituloAlternativo) " + "FROM Midia m "
			+ "WHERE m.cadastro.email = :email " + "AND (:tipos IS NULL OR m.midiaTipoNome IN :tipos) "
			+ "ORDER BY m.tituloAlternativo ASC") // <-- aqui
	Page<MidiaListagemDto> findByTiposAndUsuario(@Param("email") String email, @Param("tipos") List<String> tipos,
			Pageable pageable);

	// Retorna todas as mídias de um cadastro como DTO
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaListagemDto("
			+ "m.id, m.capaUrl, m.midiaTipo.nome, m.generos, m.tituloAlternativo) "
			+ "FROM Midia m WHERE m.cadastro = :cadastro " + "ORDER BY m.tituloAlternativo ASC") // <-- aqui
	List<MidiaListagemDto> findByCadastroDto(Cadastro cadastro);

	// Retorna mídias paginadas de um cadastro pelo email como DTO
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaListagemDto("
			+ "m.id, m.capaUrl, m.midiaTipo.nome, m.generos, m.tituloAlternativo) "
			+ "FROM Midia m WHERE m.cadastro.email = :email " + "ORDER BY m.tituloAlternativo ASC") // <-- aqui
	Page<MidiaListagemDto> findByCadastroEmailDto(String email, Pageable pageable);

	// Lista por cadastroId
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaListagemDto("
			+ "m.id, m.capaUrl, mt.nome, m.generos, m.tituloAlternativo) " + "FROM Midia m JOIN m.midiaTipo mt "
			+ "WHERE m.cadastro.id = :cadastroId " + "ORDER BY m.tituloAlternativo ASC") // <-- aqui
	List<MidiaListagemDto> listarMidiasPorCadastro(@Param("cadastroId") Long cadastroId);

	// Pesquisa por tipos e usuário (sem paginação)
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaListagemDto("
			+ "m.id, m.capaUrl, m.midiaTipoNome, m.generos, m.tituloAlternativo) " + "FROM Midia m "
			+ "WHERE m.cadastro.email = :email " + "AND (:tipos IS NULL OR m.midiaTipoNome IN :tipos) "
			+ "ORDER BY m.tituloAlternativo ASC")
	List<MidiaListagemDto> findByTiposAndUsuarioSemPaginacao(@Param("email") String email,
			@Param("tipos") List<String> tipos);

	// Conta o total de mídias de um usuário pelo email
	long countByCadastroEmail(String email);

	@Query(value = """
			    SELECT
			        m.id,
			        m.capa_url,
			        m.midia_tipo_nome,
			        m.generos,
			        m.titulo_alternativo,
			        m.nota_media
			    FROM midia m
			    WHERE m.cadastro_id = :cadastroId
			    ORDER BY m.titulo_alternativo ASC
			    LIMIT :limit OFFSET :offset
			""", nativeQuery = true)
	List<Object[]> findMidiasListagemByCadastro(@Param("cadastroId") Long cadastroId, @Param("limit") int limit,
			@Param("offset") int offset);

	@Query(value = "SELECT COUNT(*) FROM midia m WHERE m.cadastro_id = :cadastroId", nativeQuery = true)
	long countMidiasByCadastro(@Param("cadastroId") Long cadastroId);
	
	// Buscar mídias de um cadastro cujo campo "generos" contém o nome informado, retornando DTO
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaListagemMobileDto("
		       + "m.id, m.capaUrl, m.midiaTipo.nome, m.generos, m.tituloAlternativo, m.notaMedia) "
		       + "FROM Midia m "
		       + "WHERE m.cadastro.id = :cadastroId "
		       + "AND ( LOWER(REPLACE(m.generos, ' ', '')) = LOWER(REPLACE(:nomeGenero, ' ', '')) "
		       + "      OR LOWER(REPLACE(m.generos, ' ', '')) LIKE LOWER(CONCAT(REPLACE(:nomeGenero, ' ', ''), ',%')) "
		       + "      OR LOWER(REPLACE(m.generos, ' ', '')) LIKE LOWER(CONCAT('%,', REPLACE(:nomeGenero, ' ', ''), ',%')) "
		       + "      OR LOWER(REPLACE(m.generos, ' ', '')) LIKE LOWER(CONCAT('%,', REPLACE(:nomeGenero, ' ', ''))) ) "
		       + "ORDER BY m.tituloAlternativo ASC")
		Page<MidiaListagemMobileDto> buscarPorUsuarioEGeneroIgnoreCase(
		        @Param("cadastroId") Long cadastroId,
		        @Param("nomeGenero") String nomeGenero,
		        Pageable pageable);
	
	// Buscar mídias de um cadastro por tipo de midia, retornando DTO
	@Query("SELECT new com.wagnerdf.fancollectorsmedia.dto.MidiaListagemMobileDto("
		       + "m.id, m.capaUrl, m.midiaTipoNome, m.generos, m.tituloAlternativo, m.notaMedia) "
		       + "FROM Midia m "
		       + "WHERE m.cadastro.id = :cadastroId "
		       + "AND LOWER(m.midiaTipoNome) = LOWER(:tipoMidia) "
		       + "ORDER BY m.tituloAlternativo ASC")
		Page<MidiaListagemMobileDto> buscarPorUsuarioETipoMidiaIgnoreCase(
		        @Param("cadastroId") Long cadastroId,
		        @Param("tipoMidia") String tipoMidia,
		        Pageable pageable);

}
