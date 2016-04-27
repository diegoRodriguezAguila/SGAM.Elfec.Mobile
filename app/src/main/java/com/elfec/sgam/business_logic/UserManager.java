package com.elfec.sgam.business_logic;

import com.elfec.sgam.local_storage.PolicyDataStorage;
import com.elfec.sgam.model.Rule;
import com.elfec.sgam.model.User;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.web_service.RestEndpointFactory;
import com.elfec.sgam.web_service.api_endpoint.UserService;

import java.util.List;

import rx.Observable;

/**
 * Manager for user operations
 */
public class UserManager {

    private static final String QUERY_ROLES = "roles.permissions";
    private static final String QUERY_POLICY_ID = "policy_id";

    /**
     * Se conecta a la API para obtener el usuario
     * @param user usuario del que se quiere obtener la info, nota: se usaran
     * los credenciales del usuario
     * @return observable de usuario
     */
    public Observable<User> requestUser(User user){
        return requestUser(user, false);
    }

    /**
     * Se conecta a la API para obtener el usuario
     * @param user usuario del que se quiere obtener la info, nota: se usaran
     * los credenciales del usuario
     * @param withRoles si es true se obtiene el usuario con sus roles y permisos
     * @return observable de usuario
     */
    public Observable<User> requestUser(User user, boolean withRoles){
        if(!user.isAuthenticable())
            throw new IllegalArgumentException("user must be authenticable");
        return RestEndpointFactory.create(UserService.class, user)
                .getUser(user.getUsername(), withRoles?QUERY_ROLES:null);
    }

    /**
     * Se conecta a la API para obtener las reglas de directivas
     * de usuario que aplican al usuario logeado actual
     * @return observable de lista de reglas
     */
    public Observable<List<Rule>> requestPolicyRules(){
        User current = SessionManager.instance()
                .getLoggedInUser();

        return RestEndpointFactory.create(UserService.class, current)
                .getPolicyRules(current.getUsername(), QUERY_POLICY_ID);
    }

    /**
     * Syncroniza las reglas de directivas que aplican al usuario
     * logeado actual, se conecta a la API y los guarda localmente
     * @return observable de lista de reglas
     */
    public Observable<List<Rule>> syncPolicyRules(){
        final User current = SessionManager.instance()
                .getLoggedInUser();
        return requestPolicyRules()
                .flatMap(rules -> new PolicyDataStorage()
                            .saveUserPolicyRules(current.getUsername(), rules));
    }
}
