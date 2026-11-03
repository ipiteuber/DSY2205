package com.duoc.foro.repository;

import com.duoc.foro.dto.CategoriaResumen;
import com.duoc.foro.model.Categoria;
import com.duoc.foro.model.EstadoContenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio JPA de Categoria, con el resumen de temas por categoria
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    @Query("""
            SELECT new com.duoc.foro.dto.CategoriaResumen(c.id, c.nombre, COUNT(t))
            FROM Categoria c LEFT JOIN Tema t ON t.categoria = c AND t.estado = :estado
            GROUP BY c.id, c.nombre
            ORDER BY COUNT(t) DESC, c.nombre
            """)
    List<CategoriaResumen> resumenTemas(@Param("estado") EstadoContenido estado);
}
