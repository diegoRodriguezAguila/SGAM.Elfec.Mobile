package com.elfec.sgam.web_services.api_endpoints;

import com.elfec.sgam.model.User;
import com.elfec.sgam.model.web_services.RemoteSession;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Representa el endpoint de las sesiones <i>/sessions</i> en la API
 */
public interface ISessionsEndpoint {

    @POST("/sessions")
    User logIn(@Body RemoteSession session);

    @DELETE("/sessions/{id}")
    void logOut(@Path("id") String token, Callback cb);

}
