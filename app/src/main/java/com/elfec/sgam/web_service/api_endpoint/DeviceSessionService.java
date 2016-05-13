package com.elfec.sgam.web_service.api_endpoint;

import com.elfec.sgam.model.web_services.DeviceSession;

import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by drodriguez on 13/05/2016.
 * Endpoint para el inicio de sesión en dispositivos
 */
public interface DeviceSessionService {
    @POST("device_sessions")
    Observable<DeviceSession> logIn(@Query("device_id") String deviceId);

    @DELETE("device_sessions/{id}")
    Observable<Void> logOut(@Path("id") String sessionId);
}
