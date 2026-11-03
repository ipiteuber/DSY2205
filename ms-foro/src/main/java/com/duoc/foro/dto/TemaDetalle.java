package com.duoc.foro.dto;

import com.duoc.foro.model.Comentario;
import com.duoc.foro.model.Tema;

import java.util.List;

// Detalle de un tema con sus comentarios
public record TemaDetalle(Tema tema, int totalComentarios, List<Comentario> comentarios) {
}
