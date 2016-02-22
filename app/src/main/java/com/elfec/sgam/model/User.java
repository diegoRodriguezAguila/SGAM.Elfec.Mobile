package com.elfec.sgam.model;

import java.util.List;

/**
 * Created by drodriguez on 31/08/2015.
 * Modelo del usuario
 */
public class User {

    public static final String TOKEN_TYPE = "login-token";
    public static final String ACCOUNT_TYPE = "com.elfec.sgam";

    private String username;
    private String authenticationToken;
    private List<Role> roles;

    public User(){}

    public User(String username, String authenticationToken) {
        this.username = username;
        this.authenticationToken = authenticationToken;
    }

    //region Getters Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    //endregion

    /***
     * Verifica si el usuario actual se puede autenticar
     * para ello tiene que tener asignados los valores del token de autenticación
     * y el nombre de usuario
     * @return true si es autenticable
     */
    public boolean isAuthenticable(){
        return username!=null && authenticationToken!=null;
    }
}
