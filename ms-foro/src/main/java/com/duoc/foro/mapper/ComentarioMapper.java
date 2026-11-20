package com.duoc.foro.mapper;

import com.duoc.foro.dto.ComentarioDTO;
import com.duoc.foro.dto.ComentarioRequest;
import com.duoc.foro.model.Comentario;
import com.duoc.foro.model.Tema;
import org.springframework.stereotype.Component;

// Convierte entre Comentario y sus DTO usando el builder de cada clase
@Component
public class ComentarioMapper {

    public ComentarioDTO aDto(Comentario comentario) {
        return ComentarioDTO.builder()
                .id(comentario.getId())
                .idTema(comentario.getTema().getId())
                .idUsuario(comentario.getIdUsuario())
                .contenido(comentario.getContenido())
                .fecha(comentario.getFecha())
                .estado(comentario.getEstado())
                .build();
    }

    public Comentario aEntidad(ComentarioRequest datos, Tema tema) {
        return Comentario.builder()
                .tema(tema)
                .idUsuario(datos.idUsuario())
                .contenido(datos.contenido())
                .build();
    }
}
