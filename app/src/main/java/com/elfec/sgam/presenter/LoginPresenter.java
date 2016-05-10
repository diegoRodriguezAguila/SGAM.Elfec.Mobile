package com.elfec.sgam.presenter;

import com.elfec.sgam.R;
import com.elfec.sgam.business_logic.ApplicationManager;
import com.elfec.sgam.business_logic.DeviceManager;
import com.elfec.sgam.business_logic.UserManager;
import com.elfec.sgam.helpers.utils.ExceptionChecker;
import com.elfec.sgam.model.exceptions.AuthPendingDeviceException;
import com.elfec.sgam.presenter.views.ILoginView;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.web_service.ServiceErrorFactory;

import java.net.HttpURLConnection;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
             final DeviceManager deviceManager = new DeviceManager();
                 SessionManager.instance()
                     .logIn(view.getUsername(), view.getPassword())
                     .flatMap(user -> {
                         view.updateWaiting(R.string.msg_validating_device);
                         return deviceManager.validateDevice();
                     })
                     .onErrorResumeNext(t -> {
                         if (ExceptionChecker
                                 .isHttpCodeException(t, HttpURLConnection.HTTP_NOT_FOUND)) {
                             view.updateWaiting(R.string.msg_registering_device);
                             return deviceManager.registerDevice()
                                     .map(device -> {throw new AuthPendingDeviceException();});
                         }
                         return Observable.error(t);
                     })
                     .flatMap(device -> {
                         view.updateWaiting(R.string.msg_getting_policy_rules);
                         return new UserManager().syncPolicyRules();
                     })
                     .flatMap(rules -> {
                         view.updateWaiting(R.string.msg_getting_apps);
                         return new ApplicationManager().getUserPermittedApps();
                     })
                     .subscribeOn(Schedulers.newThread())
                         .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(apps -> {
                         view.hideWaiting();
                         view.userLoggedInSuccessfully(apps);
                     }, t -> {
                         view.hideWaiting();
                         t.printStackTrace();
                         view.showLoginErrors(ServiceErrorFactory.fromThrowable(t));
                     });

        } else view.notifyErrorsInFields();
    }

}
