package com.elfec.sgam.security.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.elfec.sgam.security.AccountAuthenticator;

/**
 * Servicio para la autenticación del usuario
 */
public class AuthenticationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new AccountAuthenticator(this).getIBinder();
    }
}
