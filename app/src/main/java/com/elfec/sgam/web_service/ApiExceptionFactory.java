package com.elfec.sgam.web_service;

import com.elfec.sgam.model.exceptions.ApiException;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

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
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        ApiException error = null;
        try {
            error = gson.fromJson(errorBody.string(),
                    ApiException.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error==null?DEFAULT_ERROR: error;
    }
}
