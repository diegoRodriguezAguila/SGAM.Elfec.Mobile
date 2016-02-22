package com.elfec.sgam.model.exceptions;

/**
 * Excepción que se lanza cuando el dipositivo no fué autorizado para su uso
 */
public class UnauthorizedDeviceException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Este dipositivo fue dado de baja o no fué autorizado para ingresar al sistema";
    }
}
