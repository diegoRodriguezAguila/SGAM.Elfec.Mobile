package com.elfec.sgam.security;

import android.support.annotation.NonNull;

import com.elfec.sgam.business_logic.web_services.RestEndpointFactory;
import com.elfec.sgam.business_logic.web_services.api_endpoints.ISessionsEndpoint;
import com.elfec.sgam.model.User;
import com.elfec.sgam.model.web_services.RemoteSession;

import retrofit.Callback;

/**
 * Clase que hace request del token con credenciales del usuario
 */
public class ServerTokenAuth {
    /**
     * Se conecta remotamente a los webservices para solicitar un usuario con su token
     * <p/>
     * Este método es asincrono, utilice el callback para tomar acciones
     *
     * @param username usuario a iniciar sesión
     * @param password contraseña
     * @param callback llamada al logearse
     */
    public void singIn(final String username,final String password, final @NonNull Callback<User> callback) {
        RestEndpointFactory
                .create(ISessionsEndpoint.class)
                .logIn(new RemoteSession(username, password), callback);
    }
}
