package com.elfec.sgam.business_logic;

import com.elfec.sgam.helpers.utils.ExceptionChecker;
import com.elfec.sgam.messaging.RefreshTokenReceiver;
import com.elfec.sgam.model.Device;
import com.elfec.sgam.model.exceptions.AuthPendingDeviceException;
import com.elfec.sgam.model.exceptions.UnauthorizedDeviceException;
import com.elfec.sgam.model.web_services.FcmToken;
import com.elfec.sgam.model.web_services.HttpCodes;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.settings.AppPreferences;
import com.elfec.sgam.web_service.ServiceGenerator;
import com.elfec.sgam.web_service.api_endpoint.DeviceService;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx_fcm.internal.RxFcm;

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
        return ServiceGenerator.create(DeviceService.class, SessionManager.instance()
                .getLoggedInUser())
                .getDevice(new PhysicalDeviceBuilder(
                        AppPreferences.getApplicationContext())
                        .getDeviceIdentifier())
                .map(this::checkDeviceStatus);
    }

    /**
     * Realiza las llamadas remotas para registrar este dispositivo en el servidor
     *
     * @return observable de dispositivo
     */
    public Observable<Device> registerDevice() {
        final Device physicalDevice = PhysicalDeviceBuilder.standard().buildDevice();
        return ServiceGenerator.create(DeviceService.class, SessionManager.instance()
                .getLoggedInUser())
                .registerDevice(physicalDevice)
                .flatMap(device -> syncFcmToken())
                .flatMap(voids -> Observable.just(physicalDevice));
    }

    /**
     * Gets the fcm token and synchronizes it with the api server
     * @return void Observable
     */
    public Observable<Void> syncFcmToken(){
        return RxFcm.Notifications.currentToken()
        .flatMap(this::registerFcmToken)
                .doOnCompleted(() -> RxFcm.Notifications
                        .onRefreshToken(RefreshTokenReceiver.class));
    }

    /**
     * Registers the specified fcm token for this device in the server, if the
     * registration fails because the token was already registered for this device,
     * it sends an update token request.
     * @param token token
     * @return observable
     */
    public Observable<Void> registerFcmToken(String token) {
        return ServiceGenerator.create(DeviceService.class, SessionManager.instance()
                .getLoggedInUser())
                .registerFcmToken(PhysicalDeviceBuilder.standard()
                        .getDeviceIdentifier(),
                        FcmToken.from(token))
                .onErrorResumeNext(t -> {
                    if(ExceptionChecker.isHttpCodeException(t, HttpCodes.UNPROCESSABLE_ENTITY))
                            return updateFcmToken(token);
                    return Observable.error(t);
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * Updates the specified fcm token for this device in the server
     * @param token token
     * @return observable
     */
    public Observable<Void> updateFcmToken(String token) {
        return ServiceGenerator.create(DeviceService.class, SessionManager.instance()
                .getLoggedInUser())
                .updateFcmToken(PhysicalDeviceBuilder.standard()
                                .getDeviceIdentifier(),
                        FcmToken.from(token))
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
