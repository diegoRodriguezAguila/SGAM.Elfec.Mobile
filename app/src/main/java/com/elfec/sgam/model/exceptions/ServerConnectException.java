package com.elfec.sgam.model.exceptions;


import java.net.ConnectException;

/**
 * Excepci�n que se lanza cuando no se pudo conectar al servidor por alg�n motivo
 */
public class ServerConnectException extends ConnectException {

    private String extraInfo;

    public ServerConnectException(){
        super();
    }

    public ServerConnectException(String extraInfo) {
        super();
        this.extraInfo = extraInfo;
    }

    @Override
    public String getMessage(){
        return "No se pudo establecer conexi�n con el servidor, asegurese de que est� conectado a internet!"+
                (extraInfo!=null? ("\n<i>"+extraInfo+"</i>"):"");
    }
}
