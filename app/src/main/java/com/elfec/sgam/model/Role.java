package com.elfec.sgam.model;

import com.elfec.sgam.model.data_access.SGAMDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

/**
 * Modelo de roles/perfiles de los usuarios
 */
@Table(databaseName = SGAMDataBase.NAME)
public class Role extends BaseModelWithId {

    @Column
    private String role;
    @Column
    private String description;

    private List<Permission> permissions;
    @Column
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
        if (permissions == null) {
            permissions = new Select(Permission$Table.TABLE_NAME + ".*")
                    .from(Permission.class)
                    .join(RolePermission.class, Join.JoinType.INNER)
                    .on(Condition.column(RolePermission$Table.TABLE_NAME + "." + RolePermission$Table.ROLE_ROLEID)
                            .is(this.id)) // role.id
                    .queryList();
        }
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
