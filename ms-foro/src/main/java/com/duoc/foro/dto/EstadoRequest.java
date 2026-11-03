package com.duoc.foro.dto;

import com.duoc.foro.model.EstadoContenido;
import jakarta.validation.constraints.NotNull;

// Estado nuevo de un tema o comentario (ACTIVO o BANEADO)
public record EstadoRequest(@NotNull(message = "El estado es obligatorio") EstadoContenido estado) {
}
