package com.duoc.usuarios.exception;

// Se lanza cuando el usuario no tiene permiso para la accion. ManejadorErrores la convierte a 403.
public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
