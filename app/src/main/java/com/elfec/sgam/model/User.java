package com.elfec.sgam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by drodriguez on 31/08/2015.
 * Modelo del usuario
 */
public class User {

    public static final String TOKEN_TYPE = "login-token";
    public static final String ACCOUNT_TYPE = "com.elfec.sgam";

    @Expose
    private String username;

    @SerializedName("authentication_token")
    private String authenticationToken;

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

    //endregion
}
