package com.elfec.sgam.view.launcher;

import android.content.Context;

import com.elfec.sgam.business_logic.AppDetailsCache;
import com.elfec.sgam.helpers.utils.IconFinder;
import com.elfec.sgam.model.AppDetail;
import com.elfec.sgam.settings.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Class for managin launcher apps
 */
public class LauncherApps {
    /**
     * Unique instance
     */
    private static final LauncherApps sInstance = new LauncherApps();
    /**
     * Apps caché
     */
    private List<AppDetail> mApplications;

    public static LauncherApps instance(){
        return sInstance;
    }

    private LauncherApps(){

    }

    /**
     * Asigns the app caché
     * @param applications apps
     */
    public void setAppsCache(List<AppDetail> applications){
        mApplications = applications;
        AppDetailsCache.saveApplicationsCache(applications)
        .subscribeOn(Schedulers.io())
        .subscribe();
    }

    /**
     * Gets the apps caché
     * @return app list
     */
    public Observable<List<AppDetail>> getAppsCache(){
        if(mApplications!=null){
            return Observable.just(mApplications);
        }
        return getAppsCacheFromDb();
    }

    private Observable<List<AppDetail>> getAppsCacheFromDb(){
        return AppDetailsCache.getApplicationsCache()
                .map(appDetails -> {
                    mApplications = appDetails==null? new ArrayList<>():appDetails;
                    Context context = AppPreferences.getApplicationContext();
                    IconFinder finder = new IconFinder(context);
                    for (AppDetail app : mApplications) {
                        app.setIcon(ApplicationTools.getAppIcon(app.getPackageName(), finder, context));
                    }
                    return mApplications;
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * Clears the apps caché
     */
    public void invalidateCache(){
        if (mApplications != null)
            mApplications.clear();
        mApplications = null;
    }
}
