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
import com.elfec.sgam.model.Installation;
import com.elfec.sgam.model.enums.PolicyType;
import com.elfec.sgam.security.SessionManager;
import com.elfec.sgam.security.policies.PolicyManager;
import com.elfec.sgam.settings.AppPreferences;
import com.elfec.sgam.web_service.RestEndpointFactory;
import com.elfec.sgam.web_service.api_endpoint.ApplicationService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;

import static com.elfec.sgam.business_logic.FileDownloader.*;

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
     *
     * @return observable de lista de aplicaciones
     */
    public Observable<List<AppDetail>> getAllInstalledApps() {
        return ObservableUtils.from(() -> {
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
     * Obtiene la lista de aplicaciones permitidas al usuario actual
     *
     * @return lista de aplicaiciones permitidas
     */
    public Observable<List<AppDetail>> getUserPermittedApps() {
        return getAllInstalledApps()
                .zipWith(PolicyManager
                                .getCurrentUserPolicyRules(PolicyType.APPLICATION_CONTROL),
                        RuleInterpreter::filterApps);

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

    /**
     * Downloads an apk from an application installation
     *
     * @param installation app installation request
     * @return observable of file, the file could be null if couldn't be saved successfully
     */
    public Observable<File> downloadApk(Installation installation) {
        return downloadApk(installation.getPackageName(), installation.getVersion());
    }

    /**
     * Downloads an apk from an application version
     *
     * @param packageName app package name
     * @param version     app version
     * @return observable of file, the file could be null if couldn't be saved successfully
     */
    public Observable<File> downloadApk(String packageName, String version) {
        return RestEndpointFactory.create(ApplicationService.class, SessionManager.instance()
                .getLoggedInUser())
                .downloadApk(packageName, version)
                .map(responseBody -> new FileDownloader()
                        .downloadFileToDisk(responseBody,
                                Installation.getInstallationFileName(packageName, version)));

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

    public static abstract class ApkDownloadListener implements FileDownloadListener {
        private static final short FULL = 100;
        private short mPercentage = 0;

        @Override
        public void onProgress(long fileSizeDownloaded, long totalFileSize) {
            double decPercentage = fileSizeDownloaded / totalFileSize;
            short percentage = (short) (decPercentage * FULL);
            if (percentage > mPercentage) {//it advanced
                mPercentage = percentage;
                onProgress(mPercentage, fileSizeDownloaded);
            }
        }

        /**
         * Notifies on progress, but only if there is a minimum difference of at least 1 %
         *
         * @param percentage      a value between 0 to 100, which
         *                        indicates the progress in integer percentage
         * @param downloadedSoFar bytes downloaded so far
         */
        public abstract void onProgress(short percentage, long downloadedSoFar);
    }

}
