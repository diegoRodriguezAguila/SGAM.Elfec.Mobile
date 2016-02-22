package com.elfec.sgam.security;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;

import com.elfec.sgam.model.User;
import com.elfec.sgam.settings.AppPreferences;

/**
 * Account manager para las clases usuario de la aplicacion
 */
public class UserAccountManager {
    private AccountManager mAccountManager;

    public UserAccountManager() {
        mAccountManager = AccountManager.get(AppPreferences.getApplicationContext());
    }

    /**
     * Busca entre las cuentas una que corresponda al nombre de usuario
     *
     * @param username nombre de usuario
     * @return la cuenta, si es que existe, null en caso contrario
     */
    public Account findUserAccount(String username) {
        final Account availableAccounts[] = mAccountManager.getAccountsByType(User.ACCOUNT_TYPE);
        for (Account availableAccount : availableAccounts) {
            if (availableAccount.name.equals(username))
                return availableAccount;
        }
        return null;
    }

    /**
     * Busca entre las cuentas una que corresponda al nombre de usuario
     * y retorna el usuario relacionado a la cuenta
     *
     * @param username nombre de usuario
     * @return el usuario, si es que existe, null en caso contrario
     */
    public User findUser(String username) {
        return accountToUser(findUserAccount(username));
    }

    /**
     * Registra una cuenta del usuario especificado
     *
     * @param user     usuario
     * @param password contraseña
     */
    public void registerUserAccount(User user, String password) {
        final Account account = new Account(user.getUsername(), User.ACCOUNT_TYPE);
        // Creating the account on the device and setting the auth token we got
        // (Not setting the auth token will cause another call to the server to authenticate the user)
        mAccountManager.addAccountExplicitly(account, password, null);
        mAccountManager.setAuthToken(account, User.TOKEN_TYPE, user.getAuthenticationToken());
    }

    /**
     * Verifica que el password corresponda al usuario especificado
     *
     * @param user     nombre usuario
     * @param password contraseña
     * @return true si corresponde
     */
    public boolean userPasswordIsValid(User user, String password) {
        return mAccountManager.getPassword(findUserAccount(user.getUsername())).equals(password);
    }

    /**
     * Convierte una cuenta a un User
     *
     * @param account cuenta
     * @return User
     */
    public User accountToUser(Account account) {
        if (account != null) {
            final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(account, User.TOKEN_TYPE, null, false, null, null);
            try {
                Bundle bnd = future.getResult();
                final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                return new User(account.name, authtoken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
