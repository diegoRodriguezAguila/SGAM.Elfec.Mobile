package com.elfec.sgam.presenter.views;

import java.util.List;

/**
 * Abstracci�n de la vista de logIn
 */
public interface ILoginView extends IWaitingView {
    /**
     * Obtiene el nombre de usuario
     * @return el nombre de usuario
     */
    String getUsername();

    /**
     * Obtiene el password del usuario
     * @return el password del usuario
     */
    String getPassword();


    /**
     * Notifica al usuario que hay errores en los campos
     */
    void notifyErrorsInFields();

    /**
     * Muestra los errores ocurridos durante el intento de login
     *
     * @param validationErrors errores de validaci�n de l�gin
     */
    void showLoginErrors(List<Exception> validationErrors);
}
