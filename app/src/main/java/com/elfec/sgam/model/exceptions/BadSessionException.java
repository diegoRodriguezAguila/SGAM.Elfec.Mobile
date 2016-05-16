package com.elfec.sgam.model.exceptions;

/**
 * Excepción que se lanza cuando el estado de una
 * sesión no es adecuado
 */
public class BadSessionException extends IllegalStateException {
    @Override
    public String getMessage() {
        return "La sesión registrada no es válida, o está corrupta";
    }
}
