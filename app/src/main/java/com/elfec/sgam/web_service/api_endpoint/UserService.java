package com.elfec.sgam.web_service.api_endpoint;

import com.elfec.sgam.model.Rule;
import com.elfec.sgam.model.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Representa el endpoint de usuarios <i>/users</i> en la API
 */
public interface UserService {

    @GET("users/{username}")
    Observable<User> getUser(@Path("username") String username,
                             @Query("include") String include);

    /**
     * Obtiene las directivas de usuario y sus reglas, que aplican
     * explicitamente al usuario proporcionado
     * @param username usuario
     * @return lista de directivas que aplican al usuario
     */
    @GET("users/{username}/policy_rules")
    Observable<List<Rule>> getPolicyRules(@Path("username") String username,
                                          @Query("include") String include);
}
