package com.duoc.usuarios.service;

import com.duoc.usuarios.dto.RolDTO;

import java.util.List;

// Contrato del servicio de roles
public interface RolService {

    List<RolDTO> listar();

    RolDTO buscarPorId(Long id);

    RolDTO crear(RolDTO dto);

    RolDTO actualizar(Long id, RolDTO dto);

    void eliminar(Long id);
}
