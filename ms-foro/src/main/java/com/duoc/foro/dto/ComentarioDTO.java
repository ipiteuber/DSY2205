package com.duoc.foro.dto;

import com.duoc.foro.model.EstadoContenido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO de respuesta de Comentario. Lleva solo el id del tema para no repetir el tema completo.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentarioDTO {

    private Long id;
    private Long idTema;
    private Long idUsuario;
    private String contenido;
    private LocalDateTime fecha;
    private EstadoContenido estado;
}
