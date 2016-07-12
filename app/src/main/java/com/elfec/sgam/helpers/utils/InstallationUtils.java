package com.elfec.sgam.helpers.utils;

import android.content.Intent;
import android.net.Uri;

import com.elfec.sgam.settings.AppPreferences;

import java.io.File;

/**
 * Created by drodriguez on 07/06/2016.
 * Utils for installations
 */
public class InstallationUtils {

    private static final String APK_TYPE = "application/vnd.android.package-archive";
    /**
     * Starts the installation of an app from an apk
     *
     * @param apk apk file
     */
    public static void startAppInstallation(File apk) {
        if (apk == null || !apk.exists())
            return;
        Intent intent = appInstallationIntent(apk);
        AppPreferences.getApplicationContext().startActivity(intent);
    }

    /**
     * Gets the installation intent for an apk
     * @param apk apk file
     * @return {@link Intent} for launching app installation
     */
    public static Intent appInstallationIntent(File apk){
        if (apk == null || !apk.exists())
            return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apk), APK_TYPE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * Starts the uninstallation of the app
     *
     * @param packageName app package name
     */
    public static void startAppUninstallation(String packageName) {
        if (packageName == null || packageName.isEmpty())
            return;
        Intent intent = appUninstallationIntent(packageName);
        AppPreferences.getApplicationContext().startActivity(intent);
    }

    /**
     * Gets the uninstallation intent for an app
     * @param packageName app's package name
     * @return {@link Intent} for uninstalling app
     */
    public static Intent appUninstallationIntent(String packageName){
        if (packageName == null || packageName.isEmpty())
            return null;
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(String.format("package:%s", packageName)));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
