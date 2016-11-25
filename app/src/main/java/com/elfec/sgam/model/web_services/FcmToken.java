package com.elfec.sgam.model.web_services;

/**
 * Wrapper object for gcm token
 */
public class FcmToken {
    private String token;

    public FcmToken(){

    }

    public FcmToken(String token){
        this.token = token;
    }

    /**
     * Creates a new FcmToken wrapper with the specified token
     * @param token specified token
     * @return {@link FcmToken} instance
     */
    public static FcmToken from(String token){
        return new FcmToken(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
