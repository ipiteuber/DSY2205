package com.duoc.moderacion.dto;

import com.duoc.moderacion.model.AccionModeracion;
import com.duoc.moderacion.model.TipoContenido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO de respuesta de Moderacion, con la norma aplicada
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModeracionDTO {

    private Long id;
    private TipoContenido tipoContenido;
    private Long idContenido;
    private Long idModerador;
    private AccionModeracion accion;
    private NormaDTO norma;
    private String motivo;
    private LocalDateTime fecha;
}
