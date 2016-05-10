package com.elfec.sgam.helpers.utils;

import android.app.Application;

import rx.Observable;
import rx_gcm.GcmReceiverData;
import rx_gcm.GcmReceiverUIBackground;
import rx_gcm.internal.RxGcm;

/**
 * Class created because the one who made RxGcm
 * is a genius and a retarded at the same time, such horrible code,
 * but it's better than implementing it yourself so you just suck it up. :P
 */
public class RxGcmHelper {
    /**
     * Registra el gcm token, si es que este ya fue registrado retorna inmediatamente el
     * token guardado en shared preferences
     * @param app applicacion
     * @param gcmReceiverClass gcm receiver
     * @param gcmReceiverUIBackgroundClass gcm reveiver on ui background
     * @param <T> tipo T
     * @param <U> tipo U
     * @return observable que retorna el token
     */
    public static <T extends GcmReceiverData, U extends GcmReceiverUIBackground>
    Observable<String> register(Application app, final Class<T>
            gcmReceiverClass, final Class<U> gcmReceiverUIBackgroundClass) {
        return Observable.create(subs -> {
            if (!subs.isUnsubscribed()) {
                try {
                    RxGcm.Notifications
                            .register(app, gcmReceiverClass, gcmReceiverUIBackgroundClass)
                            .subscribe(token -> {//discard result
                                }, subs::onError,
                                () -> RxGcm.Notifications.currentToken()
                                .subscribe(subs::onNext, subs::onError, subs::onCompleted));
                } catch (Throwable e) {
                    subs.onError(e);
                }
            }
        });
    }
}
