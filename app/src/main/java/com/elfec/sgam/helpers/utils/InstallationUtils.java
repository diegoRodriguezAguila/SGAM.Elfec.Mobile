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

    /**
     * Starts the installation of an app from an apk
     * @param apk apk file
     */
    public static void startAppInstallation(File apk){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppPreferences.getApplicationContext().startActivity(intent);
    }

    /**
     * Starts the uninstallation of the app
     * @param packageName app package name
     */
    public static void startAppUninstallation(String packageName){
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(String.format("package: %s", packageName)));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppPreferences.getApplicationContext().startActivity(intent);
    }
}
