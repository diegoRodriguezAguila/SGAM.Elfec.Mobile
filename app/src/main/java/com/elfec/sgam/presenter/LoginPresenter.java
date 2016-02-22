package com.elfec.sgam.presenter;

import com.elfec.sgam.R;
import com.elfec.sgam.business_logic.DeviceManager;
import com.elfec.sgam.presenter.views.ILoginView;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.web_service.api_endpoint.ServiceErrorFactory;

/**
 * Presenter para la vista de Login
 */
public class LoginPresenter {

    /**
     * Vista
     */
    private ILoginView view;

    public LoginPresenter(ILoginView view) {
        this.view = view;
    }

    /**
     * Inicia el proceso de logeo del usuario
     */
    public void logIn() {
        if (!view.getUsername().isEmpty() && !view.getPassword().isEmpty()) {
            view.showWaiting();
            SessionManager.instance().logIn(view.getUsername(), view.getPassword())
                    .doOnNext(user -> verifyDevicePermissions())
                    .subscribe(device -> {
                        view.hideWaiting();
                    }, t -> {
                        view.hideWaiting();
                        view.showLoginErrors(ServiceErrorFactory.fromThrowable(t));
                    });

        } else view.notifyErrorsInFields();
    }

    /**
     * Verifica que el dispositivo está habilitado para ingresar al sistema
     */
    private void verifyDevicePermissions() {
        view.updateWaiting(R.string.msg_validating_device);
        new DeviceManager().validateDevice().subscribe(device -> {
            view.hideWaiting();
        }, t -> {
            view.hideWaiting();
            view.showLoginErrors(ServiceErrorFactory.fromThrowable(t));
        });
    }

}
