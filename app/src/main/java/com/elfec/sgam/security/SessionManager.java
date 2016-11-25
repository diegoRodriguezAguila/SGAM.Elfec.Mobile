package com.elfec.sgam.security;

import android.support.annotation.NonNull;

import com.elfec.sgam.helpers.utils.ObservableUtils;
import com.elfec.sgam.local_storage.UserDataStorage;
import com.elfec.sgam.model.User;
import com.elfec.sgam.model.exceptions.BadSessionException;
import com.elfec.sgam.model.exceptions.InvalidPasswordException;
import com.elfec.sgam.model.web_services.RemoteSession;
import com.elfec.sgam.settings.AppPreferences;
import com.elfec.sgam.web_service.ServiceGenerator;
import com.elfec.sgam.web_service.api_endpoint.SessionService;

import java.lang.ref.SoftReference;

import rx.Observable;


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
     * No se puede instanciar esta clase directamente, debe utilizar el método {@link
     * SessionManager#instance()}
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
     * <p>
     * Este método es asincrono, utilice el callback para tomar acciones
     *
     * @param username usuario a iniciar sesión
     * @param password contraseña
     * @return observable de user
     */
    private Observable<User> logIn(String username, String password) {
        //Por ahora se deshabilitara logeo online/offline,
        //solo se permitirá loge online
        return ObservableUtils.from(()->
                new UserAccountManager().findUser(username))
        .flatMap(user->{
            if (user == null)
                return remoteLogIn(username, password);
            return localLogIn(user, password);
        });
    }

    /**
     * Se conecta remotamente a los webservices para realizar un inicio de sesión
     * En caso de ser exitoso el inicio de sesión se guarda al usuario y su token de
     * autenticación actual y se inicializa en las variables de sesión el usuario logeado
     *
     * @param username usuario
     * @param password pasword
     * @return observable de user
     */
    public Observable<User> remoteLogIn(final String username, final String password) {
        return ServiceGenerator
                .create(SessionService.class)
                .logIn(new RemoteSession(username, password))
                .flatMap(new UserDataStorage()::saveUser)
                .map(u -> {
                    new UserAccountManager().registerUserAccount(u, password);
                    setCurrentSession(u);
                    return u;
                });
    }

    /**
     * Verifica la contraseña del usuario y realiza un login local
     *
     * @param user     usuario
     * @param password contraseña
     * @return observable de user
     */
    private Observable<User> localLogIn(@NonNull User user, String password) {
        return ObservableUtils.from(()->{
            if (!new UserAccountManager().userPasswordIsValid(user, password))
                throw new InvalidPasswordException();
            setCurrentSession(user);
            return user;
        });
    }

    /**
     * Se conecta remotamente a los webservices para realizar un cierre de sesión.<br/>
     * En caso de ser exitoso el cierre de sesión se replica de forma local,
     * eliminando las variables de sesión.
     * @return observable
     */
    public Observable<Void> remoteLogOut() {
        return ObservableUtils.from(this::getLoggedInToken)
                .map(token->{
                    if(token == null)
                        throw new BadSessionException();
                    return token;
                })
                .flatMap(ServiceGenerator
                        .create(SessionService.class)
                        ::logOut)
                .map(v -> {
                    closeSession();
                    return null;
                });
    }

    /**
     * Asigna las variables de la sesión actual
     *
     * @param user usuario de la sesión actual
     */
    private void setCurrentSession(User user) {
        AppPreferences.instance()
                .setLoggedUsername(user.getUsername())
                .setLoggedToken(user.getAuthenticationToken());
    }


    /**
     * Cierra la sesión, eliminando todas las variables de sesión actuales
     */
    public void closeSession() {
        AppPreferences.instance()
                .setLoggedUsername(null)
                .setLoggedToken(null);
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
     *
     * @return null si es que ningun usuario inició sesión
     */
    public String getLoggedInToken() {
        return AppPreferences.instance().getLoggedToken();
    }

    /**
     * Obtiene el usuario logeado actual, solo obtiene
     * username y token, para obtener el usuario entero utilize
     * {@link #getFullLoggedInUser}
     *
     * @return Usuario logeado null si es que no hay ninguno
     */
    public User getLoggedInUser() {
        String username = getLoggedInUsername();
        if (username == null)
            return null;
        return new User(username, getLoggedInToken());
    }
    /**
     * Obtiene el usuario logeado actual, solo obtiene
     * username y token, para obtener el usuario entero utilize
     *
     * @return Observable del usuario logeado null si es que no hay ninguno
     */
    public Observable<User> getFullLoggedInUser() {
        return ObservableUtils.from(this::getLoggedInUsername)
                .flatMap(username ->{
                    if (username == null)
                        return Observable.just(null);
                    return new UserDataStorage().getUser(username);
                });
    }

}
