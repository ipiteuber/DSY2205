package com.duoc.usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO de Rol. Es lo que entra y sale por la API, la entidad JPA no se expone.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolDTO {

    private Long id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 30, message = "No puede superar los 30 caracteres")
    private String nombre;

    @Size(max = 200, message = "No puede superar los 200 caracteres")
    private String descripcion;
}
