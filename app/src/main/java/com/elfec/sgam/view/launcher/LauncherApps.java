package com.elfec.sgam.view.launcher;

import android.content.Context;
import android.os.Handler;

import com.elfec.sgam.business_logic.AppDetailsCache;
import com.elfec.sgam.helpers.utils.IconFinder;
import com.elfec.sgam.helpers.utils.PaletteHelper;
import com.elfec.sgam.helpers.utils.collections.ObservableCollection;
import com.elfec.sgam.model.AppDetail;
import com.elfec.sgam.settings.AppPreferences;

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
    private ObservableCollection<AppDetail> mApplications;

    public static LauncherApps instance() {
        return sInstance;
    }

    private LauncherApps() {

    }

    /**
     * Asigns the app caché
     *
     * @param applications apps
     */
    public void setAppsCache(List<AppDetail> applications) {
        mApplications = new ObservableCollection<>();
        mApplications.addAll(applications);
        AppDetailsCache.saveApplicationsCache(applications)
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    /**
     * Gets the apps caché
     *
     * @return app list
     */
    public Observable<ObservableCollection<AppDetail>> getAppsCache() {
        if (mApplications != null) {
            return Observable.just(mApplications);
        }
        return getAppsCacheFromDb();
    }

    private Observable<ObservableCollection<AppDetail>> getAppsCacheFromDb() {
        return AppDetailsCache.getApplicationsCache()
                .map(appDetails -> {
                    mApplications = new ObservableCollection<>();
                    if (appDetails != null)
                        mApplications.addAll(appDetails);
                    populateAppIcons();
                    return mApplications;
                })
                .subscribeOn(Schedulers.io());
    }

    /**
     * Schedules the icon get and bg color
     */
    private void populateAppIcons() {
        new Thread(() -> {
            Context context = AppPreferences.getApplicationContext();
            Handler handler = new Handler(context.getMainLooper());
            IconFinder finder = new IconFinder(context);
            for (int i = 0; i < mApplications.size(); i++) {
                AppDetail app = mApplications.get(i);
                app.setIcon(ApplicationTools.getAppIcon(app.getPackageName(), finder, context));
                app.setBgColor(PaletteHelper.getDrawableBackgroundColor(app.getIcon()));
                notifyChanges(handler, i);
            }
        }).start();
    }

    /**
     * Notifies changes on an index in the specified handler
     * @param handler handler
     * @param index index
     */
    private void notifyChanges(Handler handler, final int index) {
        handler.post(()->mApplications.notifyItemUpdated(index));
    }

    /**
     * Clears the apps caché
     */
    public void invalidateCache() {
        if (mApplications != null)
            mApplications.clear();
        mApplications = null;
    }
}
