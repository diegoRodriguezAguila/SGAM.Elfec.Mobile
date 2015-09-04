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
public class UserAccountsManager {
    private AccountManager mAccountManager;

    public UserAccountsManager(){
        mAccountManager = AccountManager.get(AppPreferences.getApplicationContext());
    }

    /**
     * Busca entre las cuentas una que corresponda al nombre de usuario
     * @param username nombre de usuario
     * @return la cuenta, si es que existe, null en caso contrario
     */
    public Account findUserAccount(String username){
        final Account availableAccounts[] = mAccountManager.getAccountsByType(User.ACCOUNT_TYPE);
        for (Account availableAccount : availableAccounts) {
            if (availableAccount.name.equals(username))
                return availableAccount;
        }
        return null;
    }

    /**
     * Verifica que el password corresponda al usuario especificado
     * @param user nombre usuario
     * @param password  contraseña
     * @return true si corresponde
     */
    public boolean userPasswordIsValid(User user, String password){
        return mAccountManager.getPassword(findUserAccount(user.getUsername())).equals(password);
    }

    /**
     * Convierte una cuenta a un User
     * @param account cuenta
     * @return User
     */
    public User accountToUser(Account account){
        if(account!=null) {
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
