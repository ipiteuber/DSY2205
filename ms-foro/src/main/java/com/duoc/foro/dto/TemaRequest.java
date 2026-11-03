package com.duoc.foro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Datos para publicar o editar un tema
public record TemaRequest(
        @NotBlank(message = "El título es obligatorio") @Size(min = 5, max = 150, message = "Debe tener entre 5 y 150 caracteres") String titulo,
        @NotBlank(message = "El contenido es obligatorio") @Size(max = 4000, message = "No puede superar los 4000 caracteres") String contenido,
        @NotNull(message = "La categoría es obligatoria") Long idCategoria,
        @NotNull(message = "El usuario es obligatorio") Long idUsuario) {
}
