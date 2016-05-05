package com.elfec.sgam.view.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.elfec.sgam.helpers.utils.IconFinder;
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

    /**
     * Retrieves an app icon
     * @param packageName
     * @param finder
     * @return
     */
    public static Drawable getAppIcon(String packageName, IconFinder finder) {
        return getAppIcon(packageName, finder, AppPreferences.getApplicationContext());
    }

    /**
     * Retrieves the icon
     * @param packageName
     * @param finder
     * @param context
     * @return
     */
    public static Drawable getAppIcon(String packageName, IconFinder finder, Context context){
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo result = packageManager.resolveActivity(intent, 0);
        return finder.getFullResIcon(result);
    }
}
