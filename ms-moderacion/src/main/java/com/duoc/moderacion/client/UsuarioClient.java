package com.duoc.moderacion.client;

import com.duoc.moderacion.dto.UsuarioRemoto;
import com.duoc.moderacion.exception.ReglaNegocioException;
import com.duoc.moderacion.exception.ServicioNoDisponibleException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

// Cliente REST de ms-usuarios, trae al moderador con su rol
@Component
public class UsuarioClient {

    private final RestClient restClient;

    public UsuarioClient(@Value("${servicios.usuarios.url}") String urlUsuarios) {
        this.restClient = RestClient.create(urlUsuarios);
    }

    public UsuarioRemoto obtenerUsuario(Long idUsuario) {
        try {
            return restClient.get()
                    .uri("/api/usuarios/{id}", idUsuario)
                    .retrieve()
                    .body(UsuarioRemoto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ReglaNegocioException("El usuario con ID " + idUsuario + " no existe");
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException("No se pudo conectar con ms-usuarios para validar el rol");
        }
    }
}
