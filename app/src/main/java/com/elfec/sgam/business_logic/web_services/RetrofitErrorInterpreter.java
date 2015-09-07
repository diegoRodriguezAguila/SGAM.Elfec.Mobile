package com.elfec.sgam.business_logic.web_services;

import android.nfc.FormatException;

import com.elfec.sgam.model.exceptions.ServerConnectException;
import com.elfec.sgam.model.exceptions.ServerSideException;
import com.elfec.sgam.model.web_services.RestError;

import org.apache.http.HttpStatus;

import retrofit.RetrofitError;

/**
 * Factory para inteprpretación de errores de rest services
 */
@SuppressWarnings("deprecation")
public class RetrofitErrorInterpreter {

    /**
     * Verifica que tipo de error se obtuvo, si fue de HTTP si fue de red o de conversión
     * según eso parsea un error o caso contrario retorna el mismo error
     * @param error el error original
     * @return la interpretación del error
     */
    public static Exception interpretException(RetrofitError error){
        if(error.getKind() == RetrofitError.Kind.HTTP){
            if(error.getResponse().getStatus()==HttpStatus.SC_INTERNAL_SERVER_ERROR)
                return new ServerSideException();
            RestError body = (RestError) error.getBodyAs(RestError.class);
            return new Exception(body.message);
        }
        if(error.getKind() == RetrofitError.Kind.NETWORK)
            return new ServerConnectException();
        if(error.getKind() == RetrofitError.Kind.CONVERSION)
            return new FormatException("Error al convertir la respuesta del servidor a una entidad válida\n<i>INFO: "+error.getMessage()+"</i>");
        return error;
    }

    /**
     * Obtiene el estado del error, en caso de un error de red se obtiene
     * el error de red adecuado
     * @param error error
     * @return status http
     */
    public static int getStatus(RetrofitError error) {
        if (error.getKind() == RetrofitError.Kind.NETWORK)
            return HttpStatus.SC_SERVICE_UNAVAILABLE;
        return error.getResponse().getStatus();
    }
}
