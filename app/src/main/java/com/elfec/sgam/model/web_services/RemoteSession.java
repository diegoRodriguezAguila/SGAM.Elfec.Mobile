package com.elfec.sgam.model.web_services;

/**
 * Representa una solicitud de sesion del server
 */
public class RemoteSession {

    private String username;
    private String password;

    public RemoteSession(){}

    public RemoteSession(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //region Getters y Setters
    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    //endregion

}
