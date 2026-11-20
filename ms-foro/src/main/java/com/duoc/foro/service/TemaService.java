package com.duoc.foro.service;

import com.duoc.foro.dto.TemaDTO;
import com.duoc.foro.dto.TemaDetalle;
import com.duoc.foro.dto.TemaRequest;
import com.duoc.foro.model.EstadoContenido;

import java.util.List;

// Contrato del servicio de temas
public interface TemaService {

    List<TemaDTO> listarActivos();

    List<TemaDTO> listarBaneados();

    TemaDetalle verDetalle(Long id);

    List<TemaDTO> buscarPorCategoria(Long idCategoria);

    List<TemaDTO> buscarPorTexto(String texto);

    List<TemaDTO> buscarPorUsuario(Long idUsuario);

    List<TemaDTO> populares();

    TemaDTO publicar(TemaRequest datos);

    TemaDTO actualizar(Long id, TemaRequest datos);

    void eliminar(Long id);

    TemaDTO cambiarEstado(Long id, EstadoContenido estado);
}
