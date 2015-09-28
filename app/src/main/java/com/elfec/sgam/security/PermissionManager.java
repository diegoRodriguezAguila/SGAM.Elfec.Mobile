package com.elfec.sgam.security;

import com.elfec.sgam.model.Permission;
import com.elfec.sgam.model.Role;
import com.elfec.sgam.model.User;

import java.lang.ref.SoftReference;

/**
 * Se encarga de gestionar los permisos que tiene el usuario
 */
public class PermissionManager {
    /**
     * Referencia débil utilizada para singletone
     */
    private static SoftReference<PermissionManager> permissionsManagerInstanceRef;

    /**
     * No se puede instanciar esta clase directamente, debe utilizar el método {@link PermissionManager#instance()}
     */
    private PermissionManager() {
    }

    /**
     * Devuelve la instancia actual de sessionmanager, si no existe o la instancia actual
     * ya fue recolectada por el garbage collector crea una nueva
     *
     * @return instancia del {@link PermissionManager}, nunca retorna null
     */
    public static PermissionManager instance() {
        if (permissionsManagerInstanceRef == null || permissionsManagerInstanceRef.get() == null)
            permissionsManagerInstanceRef = new SoftReference<>(new PermissionManager());
        return permissionsManagerInstanceRef.get();
    }

    /**
     * Verifica si el usuario tiene acceso de administrador al sistema
     *
     * @param user usuario a verificar
     * @return true si es que tiene el permiso
     */
    public boolean hasAdminAccessPermission(User user) {
        return hasPermission(user, Permission.ADMIN_ACCESS);
    }

    /**
     * Verifica si el usuario tiene cierto permiso en alguno de sus roles asignados
     *
     * @param user       usuario a verificar
     * @param permission permiso a verificar
     * @return true si es que tiene el permiso
     */
    public boolean hasPermission(User user, Permission permission) {
        for (Role role : user.getRoles()) {
            for (Permission perm : role.getPermissions())
                if (perm.getName().equals(permission.getName()))
                    return true;
        }
        return false;
    }
}
