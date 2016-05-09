package com.elfec.sgam.business_logic;

import android.app.Application;

import com.elfec.sgam.messaging.GcmNotificationHandler;
import com.elfec.sgam.messaging.GcmNotificationReceiver;
import com.elfec.sgam.messaging.RefreshTokenReceiver;
import com.elfec.sgam.model.Device;
import com.elfec.sgam.model.exceptions.AuthPendingDeviceException;
import com.elfec.sgam.model.exceptions.UnauthorizedDeviceException;
import com.elfec.sgam.model.web_services.GcmToken;
import com.elfec.sgam.model.web_services.HttpCodes;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.settings.AppPreferences;
import com.elfec.sgam.web_service.RestEndpointFactory;
import com.elfec.sgam.web_service.api_endpoint.DeviceService;

import java.net.HttpURLConnection;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx_gcm.internal.RxGcm;

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
                .map(this::checkDeviceStatus)
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
        final Device physicalDevice = new PhysicalDeviceBuilder(
                AppPreferences.getApplicationContext())
                .buildDevice();
        return RestEndpointFactory.create(DeviceService.class, SessionManager.instance()
                .getLoggedInUser())
                .registerDevice(physicalDevice)
                .flatMap(device -> syncGcmToken())
                .flatMap(voids -> Observable.just(physicalDevice));
    }

    /**
     * Gets the gcm token and synchronizes it with the api server
     * @return void Observable
     */
    public Observable<Void> syncGcmToken(){
        return RxGcm.Notifications
                .register((Application) AppPreferences.getApplicationContext(),
                        GcmNotificationHandler.class,
                        GcmNotificationReceiver.class)
        .flatMap(this::registerGcmToken)
                .doOnCompleted(() -> RxGcm.Notifications
                        .onRefreshToken(RefreshTokenReceiver.class));
    }

    /**
     * Registers the specified gcm token for this device in the server, if the
     * registration fails because the token was already registered for this device,
     * it sends an update token request.
     * @param token token
     * @return observable
     */
    public Observable<Void> registerGcmToken(String token) {
        return RestEndpointFactory.create(DeviceService.class, SessionManager.instance()
                .getLoggedInUser())
                .registerGcmToken(new PhysicalDeviceBuilder(
                        AppPreferences.getApplicationContext()).getDeviceIdentifier(),
                        GcmToken.from(token))
                .onErrorResumeNext(t -> {
                    if (t instanceof HttpException) {
                        if (((HttpException) t).code() == HttpCodes.UNPROCESSABLE_ENTITY)
                            return updateGcmToken(token);
                    }
                    return Observable.error(t);
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * Updates the specified gcm token for this device in the server
     * @param token token
     * @return observable
     */
    public Observable<Void> updateGcmToken(String token) {
        return RestEndpointFactory.create(DeviceService.class, SessionManager.instance()
                .getLoggedInUser())
                .updateGcmToken(new PhysicalDeviceBuilder(
                                AppPreferences.getApplicationContext()).getDeviceIdentifier(),
                        GcmToken.from(token))
                .subscribeOn(Schedulers.io());
    }


    /**
     * Verifica el estado del dispositivo, si es que no está autorizado lanza la debida excepción
     *
     * @param device {@link Device} dispositivo
     * @throws UnauthorizedDeviceException si el estado del dispositivo es no autorizado
     * @throws AuthPendingDeviceException si el dispositivo está pendiente de autorización
     */
    private Device checkDeviceStatus(Device device) throws UnauthorizedDeviceException, AuthPendingDeviceException {
        switch (device.getStatus()) {
            case UNAUTHORIZED:
                throw new UnauthorizedDeviceException();
            case AUTH_PENDING:
                throw new AuthPendingDeviceException();
        }
        return device;
    }

}
