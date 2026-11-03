package com.duoc.foro.repository;

import com.duoc.foro.model.Comentario;
import com.duoc.foro.model.EstadoContenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Repositorio JPA de Comentario
@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByTema_IdAndEstadoOrderByFechaAsc(Long idTema, EstadoContenido estado);

    List<Comentario> findByIdUsuarioOrderByFechaDesc(Long idUsuario);

    List<Comentario> findByEstadoOrderByFechaDesc(EstadoContenido estado);

    void deleteByTema_Id(Long idTema);
}
