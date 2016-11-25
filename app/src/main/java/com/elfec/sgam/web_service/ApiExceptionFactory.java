package com.elfec.sgam.web_service;

import com.elfec.sgam.model.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Creates ApiException from responses
 */
public class ApiExceptionFactory {

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    public static final ApiException DEFAULT_ERROR =
            new ApiException("Error desconocido, intente nuevamente mas tarde.", -1);

    /**
     * Construye un error a partir de un Response de retrofit
     * @param response response de retrofit
     * @return {@link ApiException} apiError
     */
    public static ApiException build(Response<?> response){
        return build(response.errorBody());
    }

    /**
     * Construye un error a partir de un errorBody de un Response
     * @param errorBody error body
     * @return {@link ApiException} apiError
     */
    public static ApiException build(ResponseBody errorBody){
        ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(
                PropertyNamingStrategy.SNAKE_CASE);
        ApiException error = null;
        try {
            error = mapper.readValue(errorBody.string(),
                    ApiException.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error==null?DEFAULT_ERROR: error;
    }
}
