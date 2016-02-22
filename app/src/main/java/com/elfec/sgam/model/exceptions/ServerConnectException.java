package com.elfec.sgam.model.exceptions;


import java.net.ConnectException;

/**
 * Excepción que se lanza cuando no se pudo conectar al servidor por algún motivo
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
        return "No se pudo establecer conexión con el servidor, asegurese de que está conectado a" +
                " internet!"+
                (extraInfo!=null? ("\n<i>"+extraInfo+"</i>"):"");
    }
}
