package com.elfec.sgam.presenter;

import com.elfec.sgam.model.User;
import com.elfec.sgam.model.callbacks.ResultCallback;
import com.elfec.sgam.presenter.views.ILoginView;
import com.elfec.sgam.security.SessionManager;

import java.util.List;

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
                SessionManager.instance().logIn(view.getUsername(), view.getPassword(), new ResultCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        view.hideWaiting();
                        //TODO correcto todo gogogo power rangers
                    }

                    @Override
                    public void onFailure(List<Exception> errors) {
                        view.hideWaiting();
                        view.showLoginErrors(errors);
                    }
                });
            }
        }).start();
        else view.notifyErrorsInFields();
    }
}
