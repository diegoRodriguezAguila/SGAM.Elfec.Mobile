package com.elfec.sgam.security;

import android.support.annotation.NonNull;

import com.elfec.sgam.business_logic.web_services.RestEndpointFactory;
import com.elfec.sgam.business_logic.web_services.api_endpoints.ISessionsEndpoint;
import com.elfec.sgam.model.User;
import com.elfec.sgam.model.callbacks.ResultCallback;
import com.elfec.sgam.model.web_services.RemoteSession;
import com.elfec.sgam.settings.AppPreferences;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
     * Se conecta remotamente a los webservices para realizar un inicio de sesi�n
     * En caso de ser exitoso el inicio de sesi�n se guarda al usuario y su token de
     * autenticaci�n actual y se inicializa en las variables de sesion el usuario logeado<br/><br/>
     * <p/>
     * Este m�todo es asincrono, utilice el callback para tomar acciones
     *
     * @param username usuario a iniciar sesi�n
     * @param password contrase�a
     * @param callback llamada al logearse
     */
    public void logIn(final String username, final String password,
                      final @NonNull ResultCallback<User> callback) {
        final List<Exception> errors = new ArrayList<>();
        RestEndpointFactory
            .create(ISessionsEndpoint.class)
            .logIn(new RemoteSession(username, password), new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    //TODO save user
                    AppPreferences.instance().setLoggedUsername(username);
                    callback.onSuccess(user);
                }

                @Override
                public void failure(RetrofitError error) {
                    errors.add(error);
                    callback.onFailure(errors);
                }
            });
    }

    /**
     * Cierra la sesi�n, eliminando todas las variables de sesi�n actuales
     */
    public void logOut() {
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
