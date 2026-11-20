package com.duoc.usuarios.service;

import com.duoc.usuarios.dto.LoginRequest;
import com.duoc.usuarios.dto.LoginResponse;
import com.duoc.usuarios.dto.UsuarioDTO;
import com.duoc.usuarios.dto.UsuarioRequest;
import com.duoc.usuarios.dto.UsuariosPorRol;

import java.util.List;

// Contrato del servicio de usuarios
public interface UsuarioService {

    List<UsuarioDTO> listar();

    UsuarioDTO buscarPorId(Long id);

    List<UsuarioDTO> buscarPorRol(String nombreRol);

    List<UsuarioDTO> buscarPorEstado(Boolean activo);

    List<UsuarioDTO> buscarPorTexto(String texto);

    List<UsuariosPorRol> resumenPorRol();

    UsuarioDTO crear(UsuarioRequest datos);

    UsuarioDTO actualizar(Long id, UsuarioRequest datos);

    void eliminar(Long id);

    LoginResponse login(LoginRequest datos);
}
