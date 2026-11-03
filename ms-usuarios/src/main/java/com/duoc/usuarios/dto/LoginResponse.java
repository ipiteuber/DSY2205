package com.duoc.usuarios.dto;

// Respuesta del login con los datos basicos del usuario y su rol
public record LoginResponse(Long idUsuario, String nombreUsuario, String nombreCompleto, String rol, String mensaje) {
}
