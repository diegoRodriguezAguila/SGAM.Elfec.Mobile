package com.elfec.sgam.business_logic.web_services;

import com.elfec.sgam.model.User;
import com.elfec.sgam.model.web_services.RestError;

import org.apache.http.HttpStatus;

import java.util.HashMap;

import retrofit.RetrofitError;

/**
 * Factory para inteprpretación de errores de rest services
 */
@SuppressWarnings("deprecation")
public class RetrofitErrorInterpreter {

    private static HashMap<String, Boolean > knownExceptions;

    static {
        knownExceptions = new HashMap<>();
        knownExceptions.put(User.class.getName()+ HttpStatus.SC_UNPROCESSABLE_ENTITY, true);
    }

    /**
     * Ve si existe una excepción customizada para interpretar
     * según la entidad y el código de error de http, en caso de no existir
     * retorna el mismo error original
     * @param entity entidad
     * @param error el error original
     * @return en caso de no existir
     * retorna el mismo error original
     */
    public static Exception interpretException(Class<?> entity, RetrofitError error){
        if(knownExceptions.get(entity.getName()+getStatus(error))) {
            RestError body = (RestError) error.getBodyAs(RestError.class);
            return new Exception(body.message);
        }
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
            return 503; // Use another code if you'd prefer
        return error.getResponse().getStatus();
    }
}
