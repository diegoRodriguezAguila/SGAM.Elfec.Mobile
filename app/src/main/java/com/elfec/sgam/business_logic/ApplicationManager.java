package com.elfec.sgam.business_logic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;

import com.elfec.sgam.helpers.utils.IconFinder;
import com.elfec.sgam.helpers.utils.ObservableUtils;
import com.elfec.sgam.helpers.utils.PaletteHelper;
import com.elfec.sgam.model.AppDetail;
import com.elfec.sgam.settings.AppPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;

/**
 * ElfecApplication manager
 */
public class ApplicationManager {

    /**
     * Comparator that compares the app names of both {@link AppDetail}
     */
    private static final Comparator<AppDetail> APP_DETAIL_COMPARATOR = (lhs, rhs) ->
            String.CASE_INSENSITIVE_ORDER.compare(lhs.getAppName().toString(),
                    rhs.getAppName().toString());

    /**
     * Obtiene todas las aplicaciones instaladas en el dispositivo
     * @return observable de lista de aplicaciones
     */
    public Observable<List<AppDetail>> getAllInstalledApps(){
        return ObservableUtils.from(()->{
            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            Context context = AppPreferences.getApplicationContext();
            PackageManager manager = context.getPackageManager();
            List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
            List<AppDetail> apps = new ArrayList<>();
            IconFinder finder = new IconFinder(context);
            for (ResolveInfo ri : availableActivities) {
                AppDetail app = getAppDetail(ri, manager, finder);
                apps.add(app);
            }
            Collections.sort(apps, APP_DETAIL_COMPARATOR);
            return apps;
        });
    }

    /**
     * Return whether the given PackgeInfo represents a system package or not.
     * User-installed packages (Market or otherwise) should not be denoted as
     * system packages.
     *
     * @param pkgInfo Package Info
     * @return true if it is a system package
     */
    public boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    @NonNull
    private AppDetail getAppDetail(ResolveInfo ri, PackageManager manager,
                                          IconFinder finder) {
        AppDetail app = new AppDetail();
        app.setAppName(ri.loadLabel(manager));
        app.setPackageName(ri.activityInfo.packageName);
        app.setIcon(finder.getFullResIcon(ri));
        app.setBgColor(PaletteHelper
                .getDrawableBackgroundColor(app.getIcon()));
        return app;
    }
}
