package com.elfec.sgam.model.exceptions;

/**
 * Excepci�n que se lanza cuando el dipositivo no fu� autorizado para su uso
 */
public class UnauthorizedDeviceException extends Exception {
    @Override
    public String getMessage() {
        return "Este dipositivo fue dado de baja o no fu� autorizado para ingresar al sistema";
    }
}
