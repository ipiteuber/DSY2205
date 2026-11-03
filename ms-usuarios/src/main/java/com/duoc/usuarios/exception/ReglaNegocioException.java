package com.duoc.usuarios.exception;

// Se lanza cuando falla una regla de negocio. ManejadorErrores la convierte a 400.
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
