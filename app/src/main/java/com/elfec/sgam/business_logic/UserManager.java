package com.elfec.sgam.business_logic;

import com.elfec.sgam.model.User;
import com.elfec.sgam.web_service.RestEndpointFactory;
import com.elfec.sgam.web_service.api_endpoint.UserService;

import rx.Observable;

/**
 * Manager for user operations
 */
public class UserManager {
    private static final String QUERY_ROLES = "roles.permissions";

    /**
     * Se conecta a la API para obtener el usuario
     * @param user usuario del que se quiere obtener la info, nota: se usaran
     * los credenciales del usuario
     * @return observable de usuario
     */
    public Observable<User> remoteGetUser(User user){
        return remoteGetUser(user, false);
    }

    /**
     * Se conecta a la API para obtener el usuario
     * @param user usuario del que se quiere obtener la info, nota: se usaran
     * los credenciales del usuario
     * @param withRoles si es true se obtiene el usuario con sus roles y permisos
     * @return observable de usuario
     */
    public Observable<User> remoteGetUser(User user, boolean withRoles){
        if(!user.isAuthenticable())
            throw new IllegalArgumentException("user must be authenticable");
        return RestEndpointFactory.create(UserService.class, user)
                .getUser(user.getUsername(), withRoles?QUERY_ROLES:null);
    }
}