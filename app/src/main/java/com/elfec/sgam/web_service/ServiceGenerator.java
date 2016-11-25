package com.elfec.sgam.web_service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.elfec.sgam.BuildConfig;
import com.elfec.sgam.helpers.utils.JacksonUtils;
import com.elfec.sgam.model.User;
import com.elfec.sgam.web_service.api_endpoint.SessionService;

import java.util.WeakHashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Factory para obtener el Endpoint de los webservices
 * Utilizando la configuración de RestAdapter necesaria
 */
public class ServiceGenerator {

    /**
     * La URL de los web services de SGAM, si fuera necesario conectar a otro webservice
     * se puede pasar otra URL
     */
    public static final String BASE_URL = BuildConfig.WS_SERVER_URL;
    /**
     * Caché para el builder, para evitar redundancia de creación
     */
    private static Retrofit.Builder sBuilder;
    /**
     * Caché para clientes http, para evitar redundancia de creación
     */
    private static WeakHashMap<String, OkHttpClient> sClients = new WeakHashMap<>();

    /**
     * Clears the clients caché. You must call this method always you need to update
     * something related to token and requests authentication
     */
    public static void invalidateCache() {
        sClients.clear();
    }

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
     * Crea un endpoint Rest  con la url por defecto {@link ServiceGenerator#BASE_URL}
     *
     * @return Endpoint
     */
    public static <T> T create(@NonNull Class<T> service) {
        return create(BASE_URL, service);
    }

    /**
     * Crea un endpoint Rest  con la url por defecto {@link ServiceGenerator#BASE_URL}
     * y con los headers de autenticación con token necesarios
     *
     * @param service  endpoint
     * @param authUser credenciales de autenticación
     * @param <T>      endpoint type
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
        //Always invalidate if it's session endpoint
        if (endpoint == SessionService.class)
            invalidateCache();
        return getBuilder()
                .baseUrl(url)
                .client(getClient(authUser))
                .build().create(endpoint);
    }

    /**
     * Obtiene el builder para retrofit
     *
     * @return {@link retrofit2.Retrofit.Builder}
     */
    @NonNull
    private static Retrofit.Builder getBuilder() {
        if (sBuilder == null) {
            sBuilder = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create(JacksonUtils.generateMapper()));
        }
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
        String username = authUser != null ? authUser.getUsername() : null;
        OkHttpClient client = sClients.get(username);
        if (client == null) {
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
                    Request.Builder builder = chain.request().newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json");
                    if (authUser != null && authUser.isAuthenticable()) {
                        builder.header("X-Api-Username", authUser.getUsername())
                                .header("X-Api-Token", authUser.getAuthenticationToken());
                    }
                    return chain.proceed(builder.build());
                }).build();
    }
}
