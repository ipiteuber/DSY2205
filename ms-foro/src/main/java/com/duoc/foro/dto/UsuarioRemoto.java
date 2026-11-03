package com.duoc.foro.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Lo que interesa de la respuesta de ms-usuarios
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioRemoto(Long id, String nombreUsuario, Boolean activo) {
}
