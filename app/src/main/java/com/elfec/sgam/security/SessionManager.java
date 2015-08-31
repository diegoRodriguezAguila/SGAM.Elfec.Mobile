package com.elfec.sgam.security;

import com.elfec.sgam.model.User;
import com.elfec.sgam.settings.AppPreferences;

import java.lang.ref.SoftReference;

/**
 * Created by drodriguez on 31/08/2015.
 * Clase que se encarga del manejo de las sesiones de los usuarios
 */
public class SessionManager {
    /**
     * Referencia d�bil utilizada para singletone
     */
    private static SoftReference<SessionManager> sessionManagerInstanceRef;

    /**
     * No se puede instanciar esta clase directamente, debe utilizar el m�todo {@link SessionManager#instance()}
     */
    private SessionManager() {
    }

    /**
     * Devuelve la isntancia actual de sessionmanager, si no existe o la instancia actual
     * ya fue recolectada por el garbage collector crea una nueva
     *
     * @return instancia del {@link SessionManager}, nunca retorna null
     */
    public static SessionManager instance() {
        if (sessionManagerInstanceRef == null || sessionManagerInstanceRef.get() == null)
            sessionManagerInstanceRef = new SoftReference<>(new SessionManager());
        return sessionManagerInstanceRef.get();
    }

    /**
     * Vericia si el usuario proporcionado ha iniciado sesi�n y es el actual
     *
     * @param username nombre de usuario a validar
     * @return true si es que el usuario logeado actual coincide con el nombre de usuario provisto
     */
    public static boolean isUserLoggedIn(String username) {
        return username != null && username.equals(AppPreferences.instance().getLoggedUsername());
    }

    /**
     * Verifica si es que alg�n usuario inici� sesi�n
     *
     * @return true si la sesi�n fue iniciada
     */
    public static boolean isSessionOpened() {
        return AppPreferences.instance().getLoggedUsername() != null;
    }

    /**
     * Inicia o sobreescribe la sesi�n para el usuario actual
     *
     * @param user usuario del que se quiere iniciar la sesi�n
     */
    public void openSession(User user) {
        AppPreferences.instance().setLoggedUsername(user.getUsername());
    }

    /**
     * Cierra la sesi�n, eliminando todas las variables de sesi�n actuales
     */
    public void closeSession() {
        AppPreferences.instance().setLoggedUsername(null);
    }

    /**
     * Obtiene el nombre de usuario logeado actual
     *
     * @return null si es que ningun usuario inici� sesi�n
     */
    public String getLoggedInUsername() {
        return AppPreferences.instance().getLoggedUsername();
    }
}
