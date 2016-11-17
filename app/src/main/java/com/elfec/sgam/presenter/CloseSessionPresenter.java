package com.elfec.sgam.presenter;

import com.elfec.sgam.R;
import com.elfec.sgam.presenter.views.ICloseSessionView;
import com.elfec.sgam.security.DeviceSessionManager;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.web_service.ServiceErrorFactory;

import static com.elfec.sgam.helpers.utils.ObservableUtils.applySchedulers;

/**
 * Created by drodriguez on 23/05/2016.
 * Close session presenter
 */
public class CloseSessionPresenter {
    /**
     * Vista
     */
    private ICloseSessionView mView;

    public CloseSessionPresenter(ICloseSessionView view) {
        this.mView = view;
    }

    /**
     * Connects to ws to close the session
     */
    public void logOut(){
        mView.showWaiting();
        DeviceSessionManager devSession = new DeviceSessionManager();
        String currentDeviceId = devSession.currentDeviceSessionId();
        devSession.logOutDevice()
            .flatMap(v -> {
                mView.updateWaiting(R.string.msg_closing_session);
                return SessionManager.instance().remoteLogOut();
            })
            .compose(applySchedulers())
            .subscribe(v -> {
                mView.hideWaiting();
                mView.onSessionClosed();
            }, t -> {
                //restore session on errors
                devSession.setCurrentDeviceSession(currentDeviceId);
                mView.hideWaiting();
                mView.showError(ServiceErrorFactory.fromThrowable(t));
            });

    }
}
