package com.duoc.moderacion.client;

import com.duoc.moderacion.exception.RecursoNoEncontradoException;
import com.duoc.moderacion.exception.ServicioNoDisponibleException;
import com.duoc.moderacion.model.TipoContenido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Map;

// Cliente REST de ms-foro. Cambia el estado (ACTIVO / BANEADO) de un tema o comentario.
@Component
public class ForoClient {

    private final RestClient restClient;

    public ForoClient(@Value("${servicios.foro.url}") String urlForo) {
        this.restClient = RestClient.create(urlForo);
    }

    public void cambiarEstado(TipoContenido tipo, Long idContenido, String estado) {
        String recurso = tipo == TipoContenido.TEMA ? "temas" : "comentarios";
        try {
            restClient.patch()
                    .uri("/api/{recurso}/{id}/estado", recurso, idContenido)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("estado", estado))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound ex) {
            String nombre = tipo == TipoContenido.TEMA ? "El tema" : "El comentario";
            throw new RecursoNoEncontradoException(nombre + " con ID " + idContenido + " no existe en el foro");
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException("No se pudo conectar con ms-foro");
        }
    }
}
