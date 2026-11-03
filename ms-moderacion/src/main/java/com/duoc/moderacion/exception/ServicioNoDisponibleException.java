package com.duoc.moderacion.exception;

// Se lanza cuando no responde otro microservicio. ManejadorErrores la convierte a 503.
public class ServicioNoDisponibleException extends RuntimeException {

    public ServicioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
