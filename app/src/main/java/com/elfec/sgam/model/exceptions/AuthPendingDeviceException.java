package com.elfec.sgam.model.exceptions;

/**
 * Excepción que se lanza cuando un dipositivo está en estado de pendiente de autorización
 */
public class AuthPendingDeviceException  extends Exception {
    @Override
    public String getMessage() {
        return "Este dipositivo está pendiente de autorización para ingresar al sistema. Si usted es administrador, proceda " +
                "a autorizar el dispositivo desde el sistema administrador, caso contrario comuníquese con uno para ello";
    }
}
