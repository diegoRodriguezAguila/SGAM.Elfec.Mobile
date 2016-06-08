package com.elfec.sgam.messaging.handlers;

import android.os.Bundle;

/**
 * Created by drodriguez on 07/06/2016.
 * notification abstraction
 */
public interface INotificationHandler {
    void handleNotification(Bundle notificationPayload);
}
