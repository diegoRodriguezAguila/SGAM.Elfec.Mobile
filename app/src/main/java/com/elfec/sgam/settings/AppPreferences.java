package com.elfec.sgam.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.elfec.sgam.model.exceptions.InitializationException;

import java.lang.ref.SoftReference;

/**
 * Maneja las sharedpreferences de toda la aplicación
 *
 * @author Diego
 */
public class AppPreferences {

    private final String LOGGED_USERNAME = "logged-username";
    private final String LOGGED_USER_TOKEN = "logged-user-token";

    /**
     * Contexto
     */
    private static Context context;
    /**
     * Referencia débil de la instancia de appPreferences
     */
    private static SoftReference<AppPreferences> appPreferencesInstanceRef;

    private SharedPreferences preferences;

    private AppPreferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    /**
     * Este método se debe llamar al inicializar la aplicación
     *
     * @param context context
     */
    public static void init(Context context) {
        AppPreferences.context = context;
    }

    /**
     * Obtiene el contexto de la aplicación
     *
     * @return el contexto de la aplicación
     */
    public static Context getApplicationContext() {
        return AppPreferences.context;
    }

    public static AppPreferences instance() {
        if (appPreferencesInstanceRef == null || appPreferencesInstanceRef.get() == null) {
            if (context == null)
                throw new InitializationException(AppPreferences.class);
            appPreferencesInstanceRef = new SoftReference<>(new AppPreferences(context));
        }
        return appPreferencesInstanceRef.get();
    }

    /**
     * Obtiene el usuario logeado actual
     *
     * @return null si es que ninguno se ha logeado
     */
    public String getLoggedUsername() {
        return preferences.getString(LOGGED_USERNAME, null);
    }

    /**
     * Asigna el usuario logeado actual, sobreescribe cualquier usuario que haya
     * sido logeado antes
     *
     * @return la instancia actual de PreferencesManager
     */
    public AppPreferences setLoggedUsername(String loggedUsername) {
        preferences.edit().putString(LOGGED_USERNAME, loggedUsername).commit();
        return this;
    }

    /**
     * Obtiene el token del usuario logeado actual
     *
     * @return null si es que ninguno se ha logeado
     */
    public String getLoggedToken() {
        return preferences.getString(LOGGED_USER_TOKEN, null);
    }

    /**
     * Asigna el token del usuario logeado actual, sobreescribe cualquier token que haya
     * sido logeado antes
     *
     * @return la instancia actual de PreferencesManager
     */
    public AppPreferences setLoggedToken(String loggedUsername) {
        preferences.edit().putString(LOGGED_USER_TOKEN, loggedUsername).commit();
        return this;
    }

    /**
     * Elimina la instancia cacheada junto con su contexto
     */
    public static void dispose() {
        context = null;
        if (appPreferencesInstanceRef != null)
            appPreferencesInstanceRef.clear();
        appPreferencesInstanceRef = null;
    }
}
