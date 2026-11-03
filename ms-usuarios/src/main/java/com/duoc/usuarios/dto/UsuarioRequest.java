package com.duoc.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Datos para crear o editar un usuario. En el PUT la password es opcional: Si viene vacia se mantiene la actual.
public record UsuarioRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio") @Size(min = 3, max = 30, message = "Debe tener entre 3 y 30 caracteres") String nombreUsuario,
        @NotBlank(message = "El email es obligatorio") @Email(message = "El email no es válido") String email,
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password,
        @NotBlank(message = "El nombre completo es obligatorio") @Size(max = 100, message = "No puede superar los 100 caracteres") String nombreCompleto,
        @NotNull(message = "El rol es obligatorio") Long idRol,
        Boolean activo) {
}
