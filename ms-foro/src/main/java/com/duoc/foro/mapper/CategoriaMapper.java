package com.duoc.foro.mapper;

import com.duoc.foro.dto.CategoriaDTO;
import com.duoc.foro.model.Categoria;
import org.springframework.stereotype.Component;

// Convierte entre Categoria y CategoriaDTO usando el builder de cada clase
@Component
public class CategoriaMapper {

    public CategoriaDTO aDto(Categoria categoria) {
        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .build();
    }

    public Categoria aEntidad(CategoriaDTO dto) {
        return Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public void actualizar(Categoria categoria, CategoriaDTO dto) {
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
    }
}
