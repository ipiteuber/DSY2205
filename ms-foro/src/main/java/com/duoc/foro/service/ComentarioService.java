package com.duoc.foro.service;

import com.duoc.foro.dto.ComentarioDTO;
import com.duoc.foro.dto.ComentarioRequest;
import com.duoc.foro.model.EstadoContenido;

import java.util.List;

// Contrato del servicio de comentarios
public interface ComentarioService {

    List<ComentarioDTO> listar();

    List<ComentarioDTO> listarBaneados();

    ComentarioDTO buscarPorId(Long id);

    List<ComentarioDTO> listarPorTema(Long idTema);

    List<ComentarioDTO> buscarPorUsuario(Long idUsuario);

    ComentarioDTO comentar(Long idTema, ComentarioRequest datos);

    ComentarioDTO actualizar(Long id, ComentarioRequest datos);

    void eliminar(Long id);

    ComentarioDTO cambiarEstado(Long id, EstadoContenido estado);
}
