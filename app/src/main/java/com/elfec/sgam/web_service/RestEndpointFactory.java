package com.elfec.sgam.web_service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.elfec.sgam.model.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Factory para obtener el Endpoint de los webservices
 * Utilizando la configuración de RestAdapter necesaria
 */
public class RestEndpointFactory {

    /**
     * La URL de los web services de SGAM, si fuera necesario conectar a otro webservice
     * se puede pasar otra URL
     */
    public static final String BASE_URL = "http://192.168.50.56:3000/api/";

    /**
     * Crea un endpoint Rest  con la url especificada
     *
     * @param url     especificada
     * @param service servicio especificado
     * @return Endpoint
     */
    public static <T> T create(@NonNull String url, @NonNull Class<T> service) {
        return create(url, service, null);
    }

    /**
     * Crea un endpoint Rest  con la url por defecto {@link RestEndpointFactory#BASE_URL}
     *
     * @return Endpoint
     */
    public static <T> T create(@NonNull Class<T> service) {
        return create(BASE_URL, service);
    }

    /**
     * Crea un endpoint Rest  con la url por defecto {@link RestEndpointFactory#BASE_URL}
     * y con los headers de autenticación con token necesarios
     *
     * @return Endpoint
     */
    public static <T> T create(@NonNull Class<T> service, User authUser) {
        return create(BASE_URL, service, authUser);
    }

    /**
     * Crea un endpoint Rest con la url especificada
     * y con los headers de autenticación con token necesarios
     *
     * @param url especificada
     * @return Instancia de endpoint
     */
    public static <T> T create(@NonNull String url, @NonNull Class<T> endpoint, @Nullable
    User authUser) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()))
                .client(buildClient(authUser))
                .build().create(endpoint);
    }

    /**
     * Crea el cliente de http para retrofit
     *
     * @param authUser credentials of API
     * @return {@link OkHttpClient} http client
     */
    @NonNull
    private static OkHttpClient buildClient(@Nullable final User authUser) {
        return new OkHttpClient().newBuilder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if (authUser != null && authUser.isAuthenticable()) {
                        request = request.newBuilder()
                                .header("X-Api-Username", authUser.getUsername())
                                .header("X-Api-Token", authUser.getAuthenticationToken())
                                .method(request.method(), request.body())
                                .build();
                    }
                    return chain.proceed(request);
                }).build();
    }
}
