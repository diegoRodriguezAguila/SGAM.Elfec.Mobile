package com.elfec.sgam.model;

import com.elfec.sgam.model.data_access.SGAMDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Tabla de union para asignación de permisos a roles
 */
@Table(databaseName = SGAMDataBase.NAME)
public class RolePermission extends BaseModelWithId {
    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "roleId",
            columnType = Long.class,
            foreignColumnName = "id")})
    private Role role;

    @Column
    @ForeignKey(references = {@ForeignKeyReference(columnName = "permissionId",
            columnType = Long.class,
            foreignColumnName = "id")})
    private Permission permission;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
