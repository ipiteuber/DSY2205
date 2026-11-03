package com.duoc.moderacion.repository;

import com.duoc.moderacion.model.Norma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio JPA de Norma
@Repository
public interface NormaRepository extends JpaRepository<Norma, Long> {
}
