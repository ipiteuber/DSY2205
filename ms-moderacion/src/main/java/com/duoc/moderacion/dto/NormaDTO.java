package com.duoc.moderacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO de Norma. Es lo que entra y sale por la API, la entidad JPA no se expone.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NormaDTO {

    private Long id;

    @NotBlank(message = "El título de la norma es obligatorio")
    @Size(max = 100, message = "No puede superar los 100 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción de la norma es obligatoria")
    @Size(max = 500, message = "No puede superar los 500 caracteres")
    private String descripcion;
}
