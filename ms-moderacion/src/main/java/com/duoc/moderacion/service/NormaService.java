package com.duoc.moderacion.service;

import com.duoc.moderacion.dto.NormaDTO;

import java.util.List;

// Contrato del servicio de normas
public interface NormaService {

    List<NormaDTO> listar();

    NormaDTO buscarPorId(Long id);

    NormaDTO crear(NormaDTO dto);

    NormaDTO actualizar(Long id, NormaDTO dto);

    void eliminar(Long id);
}
