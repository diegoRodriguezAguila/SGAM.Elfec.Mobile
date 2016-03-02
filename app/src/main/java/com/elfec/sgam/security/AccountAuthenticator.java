package com.elfec.sgam.security;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.elfec.sgam.view.Login;

import java.net.HttpURLConnection;

import rx.schedulers.Schedulers;

/**
 * Autenticador de las cuentas de elfec
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private Context mContext;

    public AccountAuthenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response,
                             String accountType, String authTokenType,
                             String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        final Intent intent = new Intent(mContext, Login.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response,
                                     Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(final AccountAuthenticatorResponse response,
                               final Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(mContext);

        String authToken = am.peekAuthToken(account, authTokenType);

        // Lets give another try to authenticate the user
        if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
                    SessionManager.instance().remoteLogIn(account.name, password)
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(user -> {
                                response.onResult(getTokenBundle(account, user.getAuthenticationToken()));
                            }, e -> {
                                response.onError(HttpURLConnection.HTTP_UNAUTHORIZED, e.getMessage());
                            });
            }
        }
        //Token válido
        else return getTokenBundle(account, authToken);
        return addAccount(response, account.type, authTokenType, null, options);
    }

    private Bundle getTokenBundle(Account account, String authToken) {
        final Bundle result = new Bundle();
        result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
        return result;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return authTokenType;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response,
                                    Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response,
                              Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
