package com.duoc.foro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO de Categoria. Es lo que entra y sale por la API, la entidad JPA no se expone.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {

    private Long id;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50, message = "No puede superar los 50 caracteres")
    private String nombre;

    @Size(max = 200, message = "No puede superar los 200 caracteres")
    private String descripcion;
}
