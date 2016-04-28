package com.elfec.sgam.helpers.ui;

import android.content.Context;
import android.os.Vibrator;

import com.elfec.sgam.settings.AppPreferences;

/**
 * Helper class to manage vibration
 */
public class VibrationHelper {
    public static final long SHORT_VIBRATION = 70;

    /**
     * Makes a small vibration based on the value of {@link #SHORT_VIBRATION}
     */
    public static void shortVibrate(){
        vibrate(SHORT_VIBRATION);
    }

    /**
     * Vibrates for the specified milliseconds
     * @param milliseconds milliseconds to vibrate
     */
    public static void vibrate(long milliseconds){
        Vibrator v = (Vibrator) AppPreferences.getApplicationContext().getSystemService(Context
                .VIBRATOR_SERVICE);
        v.vibrate(milliseconds);
    }
}
