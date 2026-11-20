package com.duoc.foro.mapper;

import com.duoc.foro.dto.TemaDTO;
import com.duoc.foro.dto.TemaRequest;
import com.duoc.foro.model.Categoria;
import com.duoc.foro.model.Tema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Convierte entre Tema y sus DTO usando el builder de cada clase
@Component
@RequiredArgsConstructor
public class TemaMapper {

    private final CategoriaMapper categoriaMapper;

    public TemaDTO aDto(Tema tema) {
        return TemaDTO.builder()
                .id(tema.getId())
                .titulo(tema.getTitulo())
                .contenido(tema.getContenido())
                .categoria(categoriaMapper.aDto(tema.getCategoria()))
                .idUsuario(tema.getIdUsuario())
                .fechaCreacion(tema.getFechaCreacion())
                .estado(tema.getEstado())
                .visitas(tema.getVisitas())
                .build();
    }

    public Tema aEntidad(TemaRequest datos, Categoria categoria) {
        return Tema.builder()
                .titulo(datos.titulo())
                .contenido(datos.contenido())
                .categoria(categoria)
                .idUsuario(datos.idUsuario())
                .build();
    }

    // Copia los datos del request sobre un tema que ya existe (para el PUT)
    public void actualizar(Tema tema, TemaRequest datos, Categoria categoria) {
        tema.setTitulo(datos.titulo());
        tema.setContenido(datos.contenido());
        tema.setCategoria(categoria);
    }
}
