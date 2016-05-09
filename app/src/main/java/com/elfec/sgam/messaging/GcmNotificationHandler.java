package com.elfec.sgam.messaging;

import android.util.Log;

import rx.Observable;
import rx_gcm.GcmReceiverData;
import rx_gcm.Message;

/**
 * Class for gcm notifications handling
 */
public class GcmNotificationHandler implements GcmReceiverData {

    @Override public Observable<Message> onNotification(Observable<Message> oMessage) {
        return oMessage.doOnNext(message -> {
            Log.d("RECIBI MENSAJE", message.payload().toString());
        });
    }

}
