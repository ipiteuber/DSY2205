package com.duoc.moderacion.exception;

// Se lanza cuando la operacion choca con datos existentes (duplicado o en uso). ManejadorErrores la convierte a 409.
public class ConflictoException extends RuntimeException {

    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
