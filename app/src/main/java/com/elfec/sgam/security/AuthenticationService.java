package com.elfec.sgam.security;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Servicio para la autenticación del usuario
 */
public class AuthenticationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new AccountAuthenticator(this).getIBinder();
    }
}
