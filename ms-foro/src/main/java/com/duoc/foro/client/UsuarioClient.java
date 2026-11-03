package com.duoc.foro.client;

import com.duoc.foro.dto.UsuarioRemoto;
import com.duoc.foro.exception.ReglaNegocioException;
import com.duoc.foro.exception.ServicioNoDisponibleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

// Cliente REST de ms-usuarios. Revisa que el autor exista y este activo antes de publicar o comentar.
@Component
public class UsuarioClient {

    private final RestClient restClient;

    public UsuarioClient(@Value("${servicios.usuarios.url}") String urlUsuarios) {
        this.restClient = RestClient.create(urlUsuarios);
    }

    public UsuarioRemoto validarUsuarioActivo(Long idUsuario) {
        UsuarioRemoto usuario;
        try {
            usuario = restClient.get()
                    .uri("/api/usuarios/{id}", idUsuario)
                    .retrieve()
                    .body(UsuarioRemoto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ReglaNegocioException("El usuario con ID " + idUsuario + " no existe");
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException("No se pudo conectar con ms-usuarios para validar al usuario");
        }
        if (usuario == null || !Boolean.TRUE.equals(usuario.activo())) {
            throw new ReglaNegocioException("El usuario con ID " + idUsuario + " está desactivado");
        }
        return usuario;
    }
}
