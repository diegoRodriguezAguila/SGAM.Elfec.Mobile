package com.elfec.sgam.model.exceptions;

/**
 * Excepci�n que se lanza cuando un dipositivo est� en estado de pendiente de autorizaci�n
 */
public class AuthPendingDeviceException  extends Exception {
    @Override
    public String getMessage() {
        return "Este dipositivo est� pendiente de autorizaci�n para su ingreso al sistema. Si usted es administrador, proceda " +
                "a autorizar el dispositivo desde el sistema administrador, caso contrario comun�quese con uno para ello";
    }
}
