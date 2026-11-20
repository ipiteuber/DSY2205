package com.duoc.foro.dto;

import java.util.List;

// Detalle de un tema con sus comentarios
public record TemaDetalle(TemaDTO tema, int totalComentarios, List<ComentarioDTO> comentarios) {
}
