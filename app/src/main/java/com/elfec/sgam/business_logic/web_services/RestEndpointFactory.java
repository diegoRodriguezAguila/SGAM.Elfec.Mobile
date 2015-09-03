package com.elfec.sgam.business_logic.web_services;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Factory para obtener el Endpoint de los webservices
 * Utilizando la configuración de RestAdapter necesaria
 *
 */
public class RestEndpointFactory {

    /**
     * La URL de los web services de SGAM, si fuera necesario conectar a otro webservice
     * se puede pasar otra URL
     */
    public static final String BASE_URL = "http://192.168.50.56:3000/api/";

    /**
     * Crea un endpoint Rest  con la url por defecto {@link RestEndpointFactory#BASE_URL}
     * @return {@link RestAdapter}
     */
    public static <T> T create(Class<T> service){
        return create(BASE_URL, service);
    }

    /**
     * Crea un endpoint Rest con la url especificada
     * @url url especificada
     * @return {@link RestAdapter}
     */
    public static <T> T create(String url, Class<T> service){
        return new RestAdapter.Builder()
                .setEndpoint(url)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                        request.addHeader("Content-Type", "application/json");
                    }
                })
                .build().create(service);
    }
}
