package com.duoc.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO de respuesta de Usuario. No lleva el hash de la password.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;
    private String nombreUsuario;
    private String email;
    private String nombreCompleto;
    private RolDTO rol;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
}
