package com.elfec.sgam.business_logic;

import com.elfec.sgam.model.Device;
import com.elfec.sgam.model.exceptions.AuthPendingDeviceException;
import com.elfec.sgam.model.exceptions.UnauthorizedDeviceException;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.settings.AppPreferences;
import com.elfec.sgam.web_service.RestEndpointFactory;
import com.elfec.sgam.web_service.api_endpoint.DeviceService;

import java.net.HttpURLConnection;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;

/**
 * Lógica de negocio de dispositivo
 */
public class DeviceManager {

    /**
     * Valida si este dispositivo está registrado y habilitado para ingresar al sistema
     *
     * @return observable de dispositivo
     */
    public Observable<Device> validateDevice() {
        return RestEndpointFactory.create(DeviceService.class, SessionManager.instance()
                .getLoggedInUser())
                .getDevice(new PhysicalDeviceBuilder(
                        AppPreferences.getApplicationContext())
                        .getDeviceIdentifier())
                .doOnNext(this::checkDeviceStatus)
                .onErrorResumeNext(t -> {
                    if (t instanceof HttpException) {
                        if (((HttpException) t).code() == HttpURLConnection.HTTP_NOT_FOUND)
                            return registerDevice();
                    }
                    return Observable.error(t);
                });
    }

    /**
     * Realiza las llamadas remotas para registrar este dispositivo en el servidor
     *
     * @return observable de dispositivo
     */
    public Observable<Device> registerDevice() {
        return RestEndpointFactory.create(DeviceService.class, SessionManager.instance()
                .getLoggedInUser())
                .registerDevice(new PhysicalDeviceBuilder(
                        AppPreferences.getApplicationContext())
                        .buildDevice());
    }


    /**
     * Verifica el estado del dispositivo, si es que no está autorizado lanza la debida excepción
     *
     * @param device {@link Device} dispositivo
     * @throws UnauthorizedDeviceException
     * @throws AuthPendingDeviceException
     */
    private void checkDeviceStatus(Device device) throws UnauthorizedDeviceException, AuthPendingDeviceException {
        switch (device.getStatus()) {
            case UNAUTHORIZED:
                throw new UnauthorizedDeviceException();
            case AUTH_PENDING:
                throw new AuthPendingDeviceException();
        }
    }

}
