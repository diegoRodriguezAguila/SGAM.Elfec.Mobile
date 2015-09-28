package com.elfec.sgam.model;

import java.util.List;

/**
 * Modelo de roles/perfiles de los usuarios
 */
public class Role {
    private String role;
    private String description;
    private List<Permission> permissions;
    private short status;


    //region Getters Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }
    //endregion
}
