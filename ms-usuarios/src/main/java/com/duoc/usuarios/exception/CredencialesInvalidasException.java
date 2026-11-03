package com.duoc.usuarios.exception;

// Se lanza cuando el usuario o la password del login no coinciden. ManejadorErrores la convierte a 401.
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
