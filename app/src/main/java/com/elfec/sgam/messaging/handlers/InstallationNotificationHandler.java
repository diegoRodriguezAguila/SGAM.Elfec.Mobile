package com.elfec.sgam.messaging.handlers;

import android.os.Bundle;
import android.util.Log;

import com.elfec.sgam.business_logic.ApplicationManager;
import com.elfec.sgam.helpers.utils.InstallationUtils;
import com.elfec.sgam.model.Installation;

import rx.schedulers.Schedulers;

/**
 * Created by drodriguez on 07/06/2016.
 * Notification handler for an installation/uninstallation request
 */
public class InstallationNotificationHandler implements INotificationHandler {
    private static final String TAG = "HANDLING INSTALLATION";
    @Override
    public void handleNotification(Bundle notificationPayload) {
        Log.d(TAG, notificationPayload.toString());
        Installation installation = new Installation(notificationPayload);
        new ApplicationManager().downloadApk(installation)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(apk->{
                    Log.d(TAG, "download successfully");
                    InstallationUtils.startAppInstallation(apk);
                    //InstallationUtils.startAppUninstallation(installation.getPackageName());
                }, Throwable::printStackTrace);
    }
}
