package com.elfec.sgam.messaging.handlers;

import android.os.Bundle;
import android.util.Log;

import com.elfec.sgam.messaging.notifications.InstallationNotifier;
import com.elfec.sgam.model.Installation;

/**
 * Created by drodriguez on 07/06/2016.
 * Notification handler for an installation/uninstallation request
 */
public class InstallationNotificationHandler implements INotificationHandler {
    private static final String TAG = "Installation Handler";
    @Override
    public void handleNotification(Bundle notificationPayload) {
        Log.d(TAG, notificationPayload.toString());
        Installation installation = new Installation(notificationPayload);
        InstallationNotifier.showApplicationUninstall(installation);
        /*new ApplicationManager().downloadApk(installation,
                InstallationNotifier.showApplicationDownload(installation))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(apk->{
                    InstallationUtils.startAppInstallation(apk);
                    //InstallationUtils.startAppUninstallation(installation.getPackageName());
                }, Throwable::printStackTrace);*/
    }
}
