package com.elfec.sgam.web_service;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.elfec.sgam.model.User;
import com.elfec.sgam.model.web_services.deserializer.UriJsonDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

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
     * Caché para el builder, para evitar redundancia de creación
     */
    private static Retrofit.Builder sBuilder;
    /**
     * Caché para clientes http, para evitar redundancia de creación
     */
    private static HashMap<String, OkHttpClient> sClients = new HashMap<>();

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
     * @param service endpoint
     * @param authUser credenciales de autenticación
     * @param <T> endpoint type
     * @return endpoint instance
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

        return getBuilder()
                .baseUrl(url)
                .client(getClient(authUser))
                .build().create(endpoint);
    }

    /**
     * Obtiene el builder para retrofit
     * @return {@link retrofit2.Retrofit.Builder}
     */
    @NonNull
    private static Retrofit.Builder getBuilder() {
        if(sBuilder==null)
            sBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(
                        new ObjectMapper().setPropertyNamingStrategy(
                                PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES)
                                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(new SimpleModule().addDeserializer(Uri.class, new
                        UriJsonDeserializer()))));
        return sBuilder;
    }

    /**
     * Obtiene el cliente de http para retrofit, si ya estaba creado devuelve la caché
     * y si no existe lo crea
     *
     * @param authUser credentials of API
     * @return {@link OkHttpClient} http client, not null
     */
    @NonNull
    private static OkHttpClient getClient(@Nullable final User authUser) {
        String username = authUser!=null? authUser.getUsername() : null;
        OkHttpClient client = sClients.get(username);
        if(client==null){
            client = buildClient(authUser);
            sClients.put(username, client);
        }
        return client;
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
                    Request.Builder builder = request.newBuilder();
                    builder.header("Accept", "application/json")
                            .header("Content-Type", "application/json");
                    if (authUser != null && authUser.isAuthenticable()) {
                        builder.header("X-Api-Username", authUser.getUsername())
                                .header("X-Api-Token", authUser.getAuthenticationToken());
                    }
                    return chain.proceed(builder
                            .method(request.method(), request.body())
                            .build());
                }).build();
    }
}
