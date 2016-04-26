package com.elfec.sgam.web_service.api_endpoint;

import com.elfec.sgam.model.User;
import com.elfec.sgam.model.web_services.RemoteSession;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Representa el endpoint de las sesiones <i>/sessions</i> en la API
 */
public interface SessionService {

    @POST("sessions")
    Observable<User> logIn(@Body RemoteSession session);

    @DELETE("sessions/{id}")
    Observable logOut(@Path("id") String token);

}
