package com.wagnerdf.fancollectorsmedia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.fancollectorsmedia.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByEmail(String email);
}
