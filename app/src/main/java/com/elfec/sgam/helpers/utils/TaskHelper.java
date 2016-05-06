package com.elfec.sgam.helpers.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.elfec.sgam.settings.AppPreferences;

/**
 * Helper para las tasks corriendo actualmente
 */
public class TaskHelper {

    /**
     * Gets the package name of the current top running activity
     * @return package name
     */
    public static String getCurrentActivity(){
        Context context = AppPreferences.getApplicationContext();
        ActivityManager manager = (ActivityManager)context.getSystemService(Context
                .ACTIVITY_SERVICE);
        return manager.getRunningAppProcesses().get(0).processName;
    }
}
