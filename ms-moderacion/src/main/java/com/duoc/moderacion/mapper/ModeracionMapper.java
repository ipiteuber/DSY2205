package com.duoc.moderacion.mapper;

import com.duoc.moderacion.dto.ModeracionDTO;
import com.duoc.moderacion.dto.ModeracionRequest;
import com.duoc.moderacion.model.AccionModeracion;
import com.duoc.moderacion.model.Moderacion;
import com.duoc.moderacion.model.Norma;
import com.duoc.moderacion.model.TipoContenido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Convierte entre Moderacion y sus DTO usando el builder de cada clase
@Component
@RequiredArgsConstructor
public class ModeracionMapper {

    private final NormaMapper normaMapper;

    public ModeracionDTO aDto(Moderacion moderacion) {
        return ModeracionDTO.builder()
                .id(moderacion.getId())
                .tipoContenido(moderacion.getTipoContenido())
                .idContenido(moderacion.getIdContenido())
                .idModerador(moderacion.getIdModerador())
                .accion(moderacion.getAccion())
                .norma(moderacion.getNorma() != null ? normaMapper.aDto(moderacion.getNorma()) : null)
                .motivo(moderacion.getMotivo())
                .fecha(moderacion.getFecha())
                .build();
    }

    public Moderacion aEntidad(TipoContenido tipo, Long idContenido, AccionModeracion accion,
                               ModeracionRequest datos, Norma norma) {
        return Moderacion.builder()
                .tipoContenido(tipo)
                .idContenido(idContenido)
                .idModerador(datos.idModerador())
                .accion(accion)
                .norma(norma)
                .motivo(datos.motivo())
                .build();
    }
}
