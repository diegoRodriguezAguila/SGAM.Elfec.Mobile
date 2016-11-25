package com.elfec.sgam.messaging;

import com.elfec.sgam.business_logic.DeviceManager;

import rx.Observable;
import rx_fcm.FcmRefreshTokenReceiver;
import rx_fcm.TokenUpdate;

/**
 * Class that refreshes the token when necessary
 */
public class RefreshTokenReceiver implements FcmRefreshTokenReceiver {

    @Override public void onTokenReceive(Observable<TokenUpdate> oTokenUpdate) {
        oTokenUpdate.flatMap((tokenUpdate ->
                new DeviceManager().updateFcmToken(tokenUpdate.getToken())))
        .subscribe(tokenUpdate -> {}, error -> {});
    }

}