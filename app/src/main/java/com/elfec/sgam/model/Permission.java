package com.elfec.sgam.model;

/**
 * Modelo de los permisos correspondientes a cada role
 */
public class Permission {
    public static final Permission ADMIN_ACCESS = new Permission ("ACCESO_APP_ADMINISTRADOR",
            "Acceso al sistema de gestión de aplicaciones móviles");

    private String name;
    private String description;
    private short status;

    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = 1;
    }

    //region Getters Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }
    //endregion
}
