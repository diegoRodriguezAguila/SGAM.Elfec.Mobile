package com.elfec.sgam.web_service.api_endpoint;

import android.support.annotation.NonNull;

import com.elfec.sgam.model.User;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Representa el endpoint de usuarios <i>/users</i> en la API
 */
public interface UserService {
    @NonNull
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @GET("users/{username}")
    Observable<User> getUser(@Path("username") String username,
                             @Query("include") String include);
}
