package com.elfec.sgam.presenter;

import com.elfec.sgam.R;
import com.elfec.sgam.business_logic.DeviceManager;
import com.elfec.sgam.model.Device;
import com.elfec.sgam.model.User;
import com.elfec.sgam.model.callbacks.ResultCallback;
import com.elfec.sgam.presenter.views.ILoginView;
import com.elfec.sgam.security.SessionManager;

import java.util.Arrays;

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
    public void logIn(){
        if (!view.getUsername().isEmpty() && !view.getPassword().isEmpty())
            new Thread(new Runnable() {
                @Override
                public void run() {
                    view.showWaiting();
                    SessionManager.instance().logIn(view.getUsername(), view.getPassword(), new ResultWithErrorCallback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            verifyDevicePermissions();
                        }
                    });
                }
            }).start();
        else view.notifyErrorsInFields();
    }

    /**
     * Verifica que el dispositivo está habilitado para ingresar al sistema
     */
    private void verifyDevicePermissions() {
        view.updateWaiting(R.string.msg_validating_device);
        new DeviceManager().validateDevice(new ResultWithErrorCallback<Device>(){
            @Override
            public void onSuccess(Device result) {
                view.hideWaiting();
            }
        });
    }

    /**
     * Callback que muestra los errores siempre que ocurren
     * @param <T>
     */
    private abstract class ResultWithErrorCallback<T> implements  ResultCallback<T>
    {
        @Override
        public void onFailure(Exception... errors) {
            view.hideWaiting();
            view.showLoginErrors(Arrays.asList(errors));
        }
    }
}
