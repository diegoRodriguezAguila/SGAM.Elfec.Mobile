package com.elfec.sgam.model.exceptions;

/**
 * Throwed when there wasn't a device session id
 */
public class NoDeviceSessionException extends IllegalStateException{
    @Override
    public String getMessage() {
        return "No existe registrada una sessión para este dispositivo!";
    }
}
