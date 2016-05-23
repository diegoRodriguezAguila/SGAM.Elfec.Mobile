package com.elfec.sgam.presenter.views;

/**
 * Created by drodriguez on 23/05/2016.
 * Main view abstraction
 */
public interface IMainView {
    /**
     * Closes the current sessión
     */
    void closeSession();

    /**
     * Checks if the main view is currently closing a session
     * @return true if it's in the middle of closing a session
     */
    boolean isClosingSession();
}
