package com.duoc.foro.service;

import com.duoc.foro.dto.CategoriaDTO;
import com.duoc.foro.dto.CategoriaResumen;

import java.util.List;

// Contrato del servicio de categorias
public interface CategoriaService {

    List<CategoriaDTO> listar();

    CategoriaDTO buscarPorId(Long id);

    List<CategoriaResumen> resumen();

    CategoriaDTO crear(CategoriaDTO dto);

    CategoriaDTO actualizar(Long id, CategoriaDTO dto);

    void eliminar(Long id);
}
