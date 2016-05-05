package com.elfec.sgam.security.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.elfec.sgam.security.UserSyncAdapter;

/**
 * Servicio para la sincronización de usuarios
 */
public class UserSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static UserSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null)
                sSyncAdapter = new UserSyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}