package com.elfec.sgam.model.web_services;

/**
 * Wrapper object for gcm token
 */
public class GcmToken {
    private String token;

    public GcmToken(){

    }

    public GcmToken(String token){
        this.token = token;
    }

    /**
     * Creates a new GcmToken wrapper with the specified token
     * @param token specified token
     * @return {@link GcmToken} instance
     */
    public static GcmToken from(String token){
        return new GcmToken(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
