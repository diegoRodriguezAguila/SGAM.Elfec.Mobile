package com.elfec.sgam.presenter.views;

/**
 * Created by drodriguez on 23/05/2016.
 * Close session abstarction view
 */
public interface ICloseSessionView extends IWaitingView {
    /**
     * Muestra el error ocurrido durante el cierre de sesión
     *
     * @param error error ocurrido durante el cierre de sesión
     */
    void showError(Exception error);

    /**
     * Notifica a la vista cuando la sesión fue cerrada exitosamente
     */
    void onSessionClosed();
}
