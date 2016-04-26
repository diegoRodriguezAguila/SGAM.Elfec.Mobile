package com.elfec.sgam.web_service.api_endpoint;

import com.elfec.sgam.model.User;

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
}
