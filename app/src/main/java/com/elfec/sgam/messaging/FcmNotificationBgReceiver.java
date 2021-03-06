package com.elfec.sgam.messaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.elfec.sgam.R;
import com.elfec.sgam.settings.AppPreferences;

import rx.Observable;
import rx_fcm.FcmReceiverUIBackground;
import rx_fcm.Message;

/**
 * Receives the notification for user showing
 */
public class FcmNotificationBgReceiver implements FcmReceiverUIBackground {
    @Override
    public void onNotification(Observable<Message> oMessage) {
        oMessage.subscribe(message -> {
            Log.d("PROCESANDO MENSAJE", message.payload().toString());
            Bundle payload = message.payload();
            String title = payload.getString("title");
            String body = payload.getString("message");
            Notification.Builder builder = new Notification.Builder(AppPreferences
                    .getApplicationContext());
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Notification notif = builder.setContentText(body).setContentTitle(title)
                    .setSound(alarmSound)
                    .setSmallIcon(R.drawable.window).build();
            NotificationManager mNotificationManager = (NotificationManager) AppPreferences
                    .getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(23, notif);
        });
    }
}