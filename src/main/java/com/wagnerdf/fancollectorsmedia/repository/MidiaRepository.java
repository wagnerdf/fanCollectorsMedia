package com.wagnerdf.fancollectorsmedia.repository;

import com.wagnerdf.fancollectorsmedia.model.Midia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MidiaRepository extends JpaRepository<Midia, Long> {
  
}

