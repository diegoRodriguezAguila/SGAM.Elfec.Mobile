package com.elfec.sgam.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by drodriguez on 31/08/2015.
 * Modelo del usuario
 */
public class User {
    @Expose
    private String username;
    @SerializedName("authentication_token")
    @Expose
    private String authenticationToken;


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
