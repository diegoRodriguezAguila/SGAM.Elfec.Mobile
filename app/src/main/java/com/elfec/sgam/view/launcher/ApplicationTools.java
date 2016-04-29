package com.elfec.sgam.view.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.elfec.sgam.settings.AppPreferences;

/**
 * Tools for apps handling
 */
public class ApplicationTools {

    /**
     * Launchs an application using the provided context
     * @param packageName the package name of the app to launch
     * @param context context for start activity
     */
    public static void launchApplication(String packageName, Context context){
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        context.startActivity(i);
    }

    /**
     * Launchs an application using the application context
     * @param packageName the package name of the app to launch
     */
    public static void launchApplication(String packageName){
        launchApplication(packageName, AppPreferences.getApplicationContext());
    }
}
