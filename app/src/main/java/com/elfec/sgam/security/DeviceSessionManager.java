package com.elfec.sgam.security;

import com.elfec.sgam.business_logic.PhysicalDeviceBuilder;
import com.elfec.sgam.helpers.utils.ObservableUtils;
import com.elfec.sgam.model.web_services.DeviceSession;
import com.elfec.sgam.settings.AppPreferences;
import com.elfec.sgam.web_service.RestEndpointFactory;
import com.elfec.sgam.web_service.api_endpoint.DeviceSessionService;

import rx.Observable;

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
                    AppPreferences.instance().setDeviceSessionId(deviceSession.getId());
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
                })
                .map(v -> {
                    closeSession();
                    return null;
                });
    }

    /**
     * Closes the local sessión
     */
    public void closeSession() {
        AppPreferences.instance().setDeviceSessionId(null);
    }
}
