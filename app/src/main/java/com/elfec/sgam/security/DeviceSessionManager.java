package com.elfec.sgam.security;

import com.elfec.sgam.business_logic.PhysicalDeviceBuilder;
import com.elfec.sgam.helpers.utils.ObservableUtils;
import com.elfec.sgam.model.web_services.DeviceSession;
import com.elfec.sgam.settings.AppPreferences;
import com.elfec.sgam.web_service.RestEndpointFactory;
import com.elfec.sgam.web_service.api_endpoint.DeviceSessionService;

import java.net.HttpURLConnection;

import rx.Observable;

import static com.elfec.sgam.helpers.utils.ExceptionChecker.isHttpCodeException;

/**
 * Created by drodriguez on 13/05/2016.
 * Manages the specific device session operations
 */
public class DeviceSessionManager {
    /**
     * Se conecta remotamente a los webservices para crear una session
     * de este dispositivo, se requiere estar autenticado para realizar esta operación
     *
     * @return observable de device session
     */
    public Observable<DeviceSession> logInDevice() {
        return RestEndpointFactory
                .create(DeviceSessionService.class, SessionManager.instance()
                        .getLoggedInUser())
                .logIn(PhysicalDeviceBuilder.standard()
                        .getDeviceIdentifier())
                .map(deviceSession -> {
                    setCurrentDeviceSession(deviceSession.getId());
                    return deviceSession;
                });
    }

    /**
     * Se conecta remotamente a los webservices para cerrar la session
     * de este dispositivo, se requiere estar autenticado para realizar esta operación.
     * Si se llama cuando no esta logeado lanza una excepción
     *
     * @return observable de device session
     */
    public Observable<Void> logOutDevice() {
        return ObservableUtils.from(AppPreferences.instance()::getDeviceSessionId)
                .flatMap(sessionId -> {
                    if (sessionId == null)
                        return ObservableUtils.from(()-> null);
                    return RestEndpointFactory
                            .create(DeviceSessionService.class,
                                    SessionManager.instance().getLoggedInUser())
                            .logOut(sessionId);
                })//if not found, session was already destroyed
                .onErrorResumeNext(t -> {
                    if (isHttpCodeException(t, HttpURLConnection.HTTP_NOT_FOUND)) {
                        return Observable.just(null);
                    }
                    return Observable.error(t);
                })
                .map(v -> {
                    closeSession();
                    return null;
                });
    }

    /**
     * Sets the current device session id
     * @param sessionId device session id
     */
    public void setCurrentDeviceSession(String sessionId) {
        AppPreferences.instance().setDeviceSessionId(sessionId);
    }

    /**
     * Gets the current device session id
     * @return current device session id, null if no session is set
     */
    public String currentDeviceSessionId(){
        return AppPreferences.instance().getDeviceSessionId();
    }

    /**
     * Closes the local sessión
     */
    public void closeSession() {
        setCurrentDeviceSession(null);
    }
}
