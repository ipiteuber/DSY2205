package com.duoc.moderacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Datos de una moderacion. idNorma es obligatoria al banear y opcional al restaurar.
public record ModeracionRequest(
        @NotNull(message = "El moderador es obligatorio") Long idModerador,
        Long idNorma,
        @NotBlank(message = "El motivo es obligatorio") @Size(max = 500, message = "No puede superar los 500 caracteres") String motivo) {
}
