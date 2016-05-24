package com.elfec.sgam.security;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.elfec.sgam.business_logic.UserManager;
import com.elfec.sgam.local_storage.UserDataStorage;
import com.elfec.sgam.model.Role;
import com.elfec.sgam.model.User;

import rx.schedulers.Schedulers;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class UserSyncAdapter extends AbstractThreadedSyncAdapter {

    private final UserAccountManager mUserAccountManager;

    /**
     * Set up the sync adapter
     */
    public UserSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mUserAccountManager = new UserAccountManager();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public UserSyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mUserAccountManager = new UserAccountManager();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        UserManager uManager = new UserManager();
        final User userToSync = mUserAccountManager.accountToUser(account);
        uManager.requestUser(userToSync, true)
                .subscribeOn(Schedulers.immediate())
                .flatMap(new UserDataStorage()::saveUser)
                .zipWith(uManager.syncPolicyRules(userToSync), (usr, rules) -> {
                    syncResult.stats.numEntries++;//user entry
                    for (Role rol : usr.getRoles()) {
                        syncResult.stats.numEntries++;
                        syncResult.stats.numEntries += rol.getPermissions().size();
                    }
                    syncResult.stats.numEntries += rules.size();
                    return syncResult.stats.numEntries;
                })
                .subscribe(updatedEntries ->
                                Log.d("Updated Entries", "Count: " + updatedEntries),
                        t -> syncResult.stats.numIoExceptions++);
    }
}
