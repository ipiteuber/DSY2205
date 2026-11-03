package com.duoc.moderacion.repository;

import com.duoc.moderacion.dto.ModeracionesPorNorma;
import com.duoc.moderacion.model.AccionModeracion;
import com.duoc.moderacion.model.Moderacion;
import com.duoc.moderacion.model.TipoContenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio JPA de Moderacion, con el resumen de baneos por norma
@Repository
public interface ModeracionRepository extends JpaRepository<Moderacion, Long> {

    List<Moderacion> findAllByOrderByFechaDesc();

    List<Moderacion> findByIdModeradorOrderByFechaDesc(Long idModerador);

    List<Moderacion> findByTipoContenidoOrderByFechaDesc(TipoContenido tipo);

    List<Moderacion> findByTipoContenidoAndIdContenidoOrderByFechaAsc(TipoContenido tipo, Long idContenido);

    boolean existsByNorma_Id(Long idNorma);

    @Query("""
            SELECT new com.duoc.moderacion.dto.ModeracionesPorNorma(n.id, n.titulo, COUNT(m))
            FROM Norma n LEFT JOIN Moderacion m ON m.norma = n AND m.accion = :accion
            GROUP BY n.id, n.titulo
            ORDER BY COUNT(m) DESC, n.titulo
            """)
    List<ModeracionesPorNorma> contarPorNorma(@Param("accion") AccionModeracion accion);
}
