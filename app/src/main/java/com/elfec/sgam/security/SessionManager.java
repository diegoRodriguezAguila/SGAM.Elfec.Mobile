package com.elfec.sgam.security;

import android.support.annotation.NonNull;

import com.elfec.sgam.business_logic.web_services.RetrofitErrorInterpreter;
import com.elfec.sgam.model.User;
import com.elfec.sgam.model.callbacks.ResultCallback;
import com.elfec.sgam.model.exceptions.InvalidPasswordException;
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
     * Referencia débil utilizada para singletone
     */
    private static SoftReference<SessionManager> sessionManagerInstanceRef;

    /**
     * No se puede instanciar esta clase directamente, debe utilizar el método {@link SessionManager#instance()}
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
     * Vericia si el usuario proporcionado ha iniciado sesión y es el actual
     *
     * @param username nombre de usuario a validar
     * @return true si es que el usuario logeado actual coincide con el nombre de usuario provisto
     */
    public static boolean isUserLoggedIn(String username) {
        return username != null && username.equals(AppPreferences.instance().getLoggedUsername());
    }

    /**
     * Verifica si es que algún usuario inició sesión
     *
     * @return true si la sesión fue iniciada
     */
    public static boolean isSessionOpened() {
        return AppPreferences.instance().getLoggedUsername() != null;
    }

    /**
     * Busca la cuenta de usuario local, si existe se realiza un logeo local, caso contrario
     * se conecta remotamente a los webservices para realizar un inicio de sesión
     * En caso de ser exitoso el inicio de sesión se guarda al usuario y su token de
     * autenticación actual y se inicializa en las variables de sesion el usuario logeado<br/><br/>
     * <p/>
     * Este método es asincrono, utilice el callback para tomar acciones
     *
     * @param username usuario a iniciar sesión
     * @param password contraseña
     * @param callback llamada al logearse
     */
    public void logIn(String username, String password, final @NonNull ResultCallback<User> callback) {
        final UserAccountsManager uAccManager = new UserAccountsManager();
        User user = uAccManager.accountToUser(uAccManager.findUserAccount(username));
        if(user==null)
            remoteLogIn(uAccManager, username, password, callback);
        else localLogIn(uAccManager, user, password, callback);
    }

    /**
     * Se conecta remotamente a los webservices para realizar un inicio de sesión
     * En caso de ser exitoso el inicio de sesión se guarda al usuario y su token de
     * autenticación actual y se inicializa en las variables de sesion el usuario logeado
     * @param uAccManager user account manager
     * @param username usuario a iniciar sesión
     * @param password contraseña
     * @param callback llamada al logearse
     */
    private void remoteLogIn(final UserAccountsManager uAccManager, String username,
                             final String password, @NonNull final ResultCallback<User> callback) {
        final List<Exception> errors = new ArrayList<>();
        new ServerTokenAuth().singIn(username, password, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                setCurrentSession(user);
                uAccManager.registerUserAccount(user, password);
                callback.onSuccess(user);
            }
            @Override
            public void failure(RetrofitError error) {
                errors.add(RetrofitErrorInterpreter.interpretException(error));
                callback.onFailure(errors);
            }
        });
    }

    /**
     * Verifica la contraseña del usuario y realiza un login local
     * @param uAccManager  user account manager
     * @param user usuario
     * @param password contraseña
     * @param callback llamada al logearse
     */
    private void localLogIn(final UserAccountsManager uAccManager, @NonNull User user,
                            String password,
                            @NonNull ResultCallback<User> callback) {
        final List<Exception> errors = new ArrayList<>();
        if(uAccManager.userPasswordIsValid(user, password)) {
            setCurrentSession(user);
            callback.onSuccess(user);
        }
        else{
            errors.add(new InvalidPasswordException());
            callback.onFailure(errors);
        }
    }

    /**
     * Asigna las variables de la sesión actual
     * @param user usuario de la sesión actual
     */
    private void setCurrentSession(User user) {
        AppPreferences.instance().setLoggedUsername(user.getUsername());
        AppPreferences.instance().setLoggedToken(user.getAuthenticationToken());
    }


    /**
     * Cierra la sesión, eliminando todas las variables de sesión actuales
     */
    public void logOut() {
        AppPreferences.instance().setLoggedUsername(null);
    }

    /**
     * Obtiene el nombre de usuario logeado actual
     *
     * @return null si es que ningun usuario inició sesión
     */
    public String getLoggedInUsername() {
        return AppPreferences.instance().getLoggedUsername();
    }

    /**
     * Obtiene el token del usuario logeado actual
     * @return null si es que ningun usuario inició sesión
     */
    public String getLoggedInToken(){
        return AppPreferences.instance().getLoggedToken();
    }
}
