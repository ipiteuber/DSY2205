package com.duoc.foro.dto;

import com.duoc.foro.model.EstadoContenido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO de respuesta de Tema, con su categoria
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemaDTO {

    private Long id;
    private String titulo;
    private String contenido;
    private CategoriaDTO categoria;
    private Long idUsuario;
    private LocalDateTime fechaCreacion;
    private EstadoContenido estado;
    private Integer visitas;
}
