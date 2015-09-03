package com.elfec.sgam.presenter.views;

/**
 * Abstracción de la vista de logIn
 */
public interface ILoginView {
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
}
