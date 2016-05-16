package com.elfec.sgam.helpers.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.elfec.sgam.settings.AppPreferences;

/**
 * Alarm helper methods for {@link AlarmManager}
 */
public class AlarmHelper {
    /**
     * Uses the alarm manager to schedule this app restart
     *
     * @param miliseconds miliseconds of schedule
     */
    public static void scheduleAppRestart(int miliseconds) {
        Context context = AppPreferences.getApplicationContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + miliseconds,
                pendingIntent);
    }
}
