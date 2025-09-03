package com.wagnerdf.fancollectorsmedia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagnerdf.fancollectorsmedia.model.ApiConfig;

public interface ApiConfigRepository extends JpaRepository<ApiConfig, Long> {
    Optional<ApiConfig> findByChave(String chave);
}
