package com.elfec.sgam.messaging;

import com.elfec.sgam.messaging.handlers.INotificationHandler;
import com.elfec.sgam.messaging.handlers.NotificationHandlerFactory;

import rx.Observable;
import rx_gcm.GcmReceiverData;
import rx_gcm.Message;

/**
 * Class for gcm notifications handling
 */
public class GcmNotificationReceiver implements GcmReceiverData {

    public static final String NOTIFICATION_TYPE_KEY = "type";

    @Override
    public Observable<Message> onNotification(Observable<Message> oMessage) {
        return oMessage.map(message -> {
            String type = message.payload().getString(NOTIFICATION_TYPE_KEY);
            if (type != null) {
                INotificationHandler handler = NotificationHandlerFactory.create(type);
                if (handler != null)
                    handler.handleNotification(message.payload());
            }
            return message;
        });
    }

}
