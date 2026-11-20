package com.duoc.moderacion.mapper;

import com.duoc.moderacion.dto.NormaDTO;
import com.duoc.moderacion.model.Norma;
import org.springframework.stereotype.Component;

// Convierte entre Norma y NormaDTO usando el builder de cada clase
@Component
public class NormaMapper {

    public NormaDTO aDto(Norma norma) {
        return NormaDTO.builder()
                .id(norma.getId())
                .titulo(norma.getTitulo())
                .descripcion(norma.getDescripcion())
                .build();
    }

    public Norma aEntidad(NormaDTO dto) {
        return Norma.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public void actualizar(Norma norma, NormaDTO dto) {
        norma.setTitulo(dto.getTitulo());
        norma.setDescripcion(dto.getDescripcion());
    }
}
