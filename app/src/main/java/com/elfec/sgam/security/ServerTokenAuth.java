package com.elfec.sgam.security;

import com.elfec.sgam.model.User;
import com.elfec.sgam.model.web_services.RemoteSession;
import com.elfec.sgam.web_services.RestEndpointFactory;
import com.elfec.sgam.web_services.api_endpoints.ISessionsEndpoint;

/**
 * Clase que hace request del token con credenciales del usuario
 */
public class ServerTokenAuth {
    /**
     * Se conecta remotamente a los webservices para solicitar un usuario con su token
     * <p/>
     * Este m�todo es asincrono, utilice el callback para tomar acciones
     *
     * @param username usuario a iniciar sesi�n
     * @param password contrase�a
     */
    public User singIn(final String username,final String password) {
        return RestEndpointFactory
                .create(ISessionsEndpoint.class)
                .logIn(new RemoteSession(username, password));
    }
}
