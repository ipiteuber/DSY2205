package com.duoc.foro.dto;

// Fila del resumen de temas activos por categoria
public record CategoriaResumen(Long idCategoria, String categoria, Long temasActivos) {
}
