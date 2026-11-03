package com.duoc.usuarios.dto;

import jakarta.validation.constraints.NotBlank;

// Datos del login. "usuario" acepta el nombre de usuario o el email.
public record LoginRequest(
        @NotBlank(message = "El usuario es obligatorio") String usuario,
        @NotBlank(message = "La contraseña es obligatoria") String password) {
}
