package com.duoc.foro.repository;

import com.duoc.foro.model.EstadoContenido;
import com.duoc.foro.model.Tema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio JPA de Tema, con la busqueda por texto
@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {

    List<Tema> findByEstadoOrderByFechaCreacionDesc(EstadoContenido estado);

    List<Tema> findByCategoria_IdAndEstadoOrderByFechaCreacionDesc(Long idCategoria, EstadoContenido estado);

    List<Tema> findByIdUsuarioOrderByFechaCreacionDesc(Long idUsuario);

    List<Tema> findTop5ByEstadoOrderByVisitasDesc(EstadoContenido estado);

    boolean existsByCategoria_Id(Long idCategoria);

    @Query("""
            SELECT t FROM Tema t
            WHERE t.estado = :estado
              AND (LOWER(t.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
                   OR LOWER(t.contenido) LIKE LOWER(CONCAT('%', :texto, '%')))
            ORDER BY t.fechaCreacion DESC
            """)
    List<Tema> buscarPorTexto(@Param("texto") String texto, @Param("estado") EstadoContenido estado);
}
