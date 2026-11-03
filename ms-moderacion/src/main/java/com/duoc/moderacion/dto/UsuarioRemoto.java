package com.duoc.moderacion.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Lo que interesa de la respuesta de ms-usuarios, incluido el rol
@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioRemoto(Long id, String nombreUsuario, Boolean activo, RolRemoto rol) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RolRemoto(Long id, String nombre) {
    }

    public String nombreRol() {
        return rol != null ? rol.nombre() : "";
    }
}
