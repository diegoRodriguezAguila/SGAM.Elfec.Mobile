package com.elfec.sgam.model;

import android.net.Uri;

import java.util.List;

/**
 * Created by drodriguez on 31/08/2015.
 * Modelo del usuario
 */
public class User implements Entity {

    public static final String TOKEN_TYPE = "login-token";
    public static final String ACCOUNT_TYPE = "com.elfec.sgam";

    private String username;
    private String authenticationToken;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private Uri photoUrl;
    private String companyArea;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCompanyArea() {
        return companyArea;
    }

    public void setCompanyArea(String companyArea) {
        this.companyArea = companyArea;
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


    //region Entity
    @Override
    public String getId() {
        return username;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public String getDetails() {
        return getFullName();
    }

    //endregion
}
