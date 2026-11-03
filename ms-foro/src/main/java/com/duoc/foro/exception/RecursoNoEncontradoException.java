package com.duoc.foro.exception;

// Se lanza cuando no se encuentra un recurso. ManejadorErrores la convierte a 404.
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
