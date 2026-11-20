package com.duoc.usuarios.mapper;

import com.duoc.usuarios.dto.UsuarioDTO;
import com.duoc.usuarios.dto.UsuarioRequest;
import com.duoc.usuarios.model.Rol;
import com.duoc.usuarios.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Convierte entre Usuario y sus DTO usando el builder de cada clase. La password la encripta el servicio.
@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private final RolMapper rolMapper;

    public UsuarioDTO aDto(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nombreUsuario(usuario.getNombreUsuario())
                .email(usuario.getEmail())
                .nombreCompleto(usuario.getNombreCompleto())
                .rol(rolMapper.aDto(usuario.getRol()))
                .activo(usuario.getActivo())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }

    public Usuario aEntidad(UsuarioRequest datos, Rol rol, String passwordHash) {
        return Usuario.builder()
                .nombreUsuario(datos.nombreUsuario())
                .email(datos.email())
                .nombreCompleto(datos.nombreCompleto())
                .rol(rol)
                .activo(datos.activo())
                .passwordHash(passwordHash)
                .build();
    }

    // Copia los datos del request sobre un usuario que ya existe (para el PUT)
    public void actualizar(Usuario usuario, UsuarioRequest datos, Rol rol) {
        usuario.setNombreUsuario(datos.nombreUsuario());
        usuario.setEmail(datos.email());
        usuario.setNombreCompleto(datos.nombreCompleto());
        usuario.setRol(rol);
        if (datos.activo() != null) {
            usuario.setActivo(datos.activo());
        }
    }
}
