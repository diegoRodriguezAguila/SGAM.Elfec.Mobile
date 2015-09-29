package com.elfec.sgam.web_services;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

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
     * Crea un endpoint Rest  con la url especificada
     * @url url especificada
     * @return {@link RestAdapter}
     */
    public static <T> T create(String url, Class<T> service){
        return create(url, service, null, null);
    }

    /**
     * Crea un endpoint Rest  con la url por defecto {@link RestEndpointFactory#BASE_URL}
     * @return {@link RestAdapter}
     */
    public static <T> T create(Class<T> service){
        return create(BASE_URL, service);
    }

    /**
     * Crea un endpoint Rest  con la url por defecto {@link RestEndpointFactory#BASE_URL}
     * y con los headers de autenticación con token necesarios
     * @return {@link RestAdapter}
     */
    public static <T> T create(Class<T> service, String username, String authToken){
        return create(BASE_URL, service, username, authToken);
    }

    /**
     * Crea un endpoint Rest con la url especificada
     * y con los headers de autenticación con token necesarios
     * @url url especificada
     * @return {@link RestAdapter}
     */
    public static <T> T create(String url, Class<T> service,final String username,final String authToken){
        return new RestAdapter.Builder()
                .setEndpoint(url)
                .setConverter(new GsonConverter(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .setExclusionStrategies(new ExclusionStrategy() {
                            @Override
                            public boolean shouldSkipField(FieldAttributes f) {
                                return f.getDeclaredClass().equals(ModelAdapter.class);
                            }
                            @Override
                            public boolean shouldSkipClass(Class<?> clazz) { return false; }
                        })
                        .create()))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                        request.addHeader("Content-Type", "application/json");
                        if (username != null && authToken != null) {
                            request.addHeader("X-Api-Username", username);
                            request.addHeader("X-Api-Token", authToken);
                        }
                    }
                })
                .build().create(service);
    }
}
