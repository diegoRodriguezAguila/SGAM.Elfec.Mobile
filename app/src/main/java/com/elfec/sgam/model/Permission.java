package com.elfec.sgam.model;

import com.elfec.sgam.model.data_access.SGAMDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Modelo de los permisos correspondientes a cada role
 */
@Table(databaseName = SGAMDataBase.NAME)
public class Permission extends BaseModelWithId {
    public static final Permission ADMIN_ACCESS = new Permission ("ACCESO_APP_ADMINISTRADOR",
            "Acceso al sistema de gestión de aplicaciones móviles");
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private short status;

    public Permission(){
        super();
    }

    public Permission(String name, String description) {
        super();
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
