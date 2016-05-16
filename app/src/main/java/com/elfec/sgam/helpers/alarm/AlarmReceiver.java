package com.elfec.sgam.helpers.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.elfec.sgam.view.Main;

/**
 * Alarm actions receiver
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String WITH_ERROR_MSG = "with_error_msg";

    @Override
    public void onReceive(Context context, Intent intent) {

        // Start the MainActivity
        Intent i = new Intent(context, Main.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}