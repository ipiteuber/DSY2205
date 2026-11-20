package com.duoc.usuarios.mapper;

import com.duoc.usuarios.dto.RolDTO;
import com.duoc.usuarios.model.Rol;
import org.springframework.stereotype.Component;

// Convierte entre Rol y RolDTO usando el builder de cada clase
@Component
public class RolMapper {

    public RolDTO aDto(Rol rol) {
        return RolDTO.builder()
                .id(rol.getId())
                .nombre(rol.getNombre())
                .descripcion(rol.getDescripcion())
                .build();
    }

    public Rol aEntidad(RolDTO dto) {
        return Rol.builder()
                .nombre(dto.getNombre().toUpperCase())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public void actualizar(Rol rol, RolDTO dto) {
        rol.setNombre(dto.getNombre().toUpperCase());
        rol.setDescripcion(dto.getDescripcion());
    }
}
