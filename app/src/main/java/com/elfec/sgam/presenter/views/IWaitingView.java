package com.elfec.sgam.presenter.views;

import android.support.annotation.StringRes;

/**
 * Abstracción de una vista en la que se debe realizar una espera
 */
public interface IWaitingView {
    /**
     * Indica al usuario que debe esperar
     */
    void showWaiting();

    /**
     * Acualiza el mensaje de espera del usuario
     */
    void updateWaiting(@StringRes int strId);

    /**
     * Borra el mensaje de espera
     */
    void hideWaiting();
}
