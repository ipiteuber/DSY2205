package com.duoc.foro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Datos para crear o editar un comentario
public record ComentarioRequest(
        @NotBlank(message = "El comentario no puede estar vacío") @Size(max = 1000, message = "No puede superar los 1000 caracteres") String contenido,
        @NotNull(message = "El usuario es obligatorio") Long idUsuario) {
}
