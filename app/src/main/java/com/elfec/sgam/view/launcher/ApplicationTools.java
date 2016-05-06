package com.elfec.sgam.view.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.utils.IconFinder;
import com.elfec.sgam.settings.AppPreferences;

import java.io.Closeable;
import java.util.List;

/**
 * Tools for apps handling
 */
public class ApplicationTools {
    private static final Drawable sDefaultIcon = ContextCompat.getDrawable(AppPreferences
            .getApplicationContext(), R.drawable.window);
    /**
     * Launchs an application using the provided context
     * @param packageName the package name of the app to launch
     * @param context context for start activity
     */
    public static void launchApplication(String packageName, Context context){
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        context.startActivity(i);
    }

    /**
     * Launchs an application using the application context
     * @param packageName the package name of the app to launch
     */
    public static void launchApplication(String packageName){
        launchApplication(packageName, AppPreferences.getApplicationContext());
    }

    /**
     * Gets the label of the app, if couldn't find an app mathing the provided
     * package name it returns the packageName;
     * @param packageName package name
     * @return App's label (name)
     */
    public static String getAppLabel(String packageName) {
        return getAppLabel(packageName, AppPreferences.getApplicationContext());
    }

    /**
     * Gets the label of the app, if couldn't find an app mathing the provided
     * package name it returns the packageName;
     * @param packageName package name
     * @param context context
     * @return App's label (name)
     */
    public static String getAppLabel(String packageName, Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            return applicationInfo.loadLabel(packageManager).toString();
        } catch (PackageManager.NameNotFoundException e){
            return packageName;
        }
    }
    /**
     * Gets the default apps icon
     * @return {@link Drawable} default app icon
     */
    public static Drawable getAppDefaultIcon(){
        return sDefaultIcon;
    }

    /**
     * Gets the icon retriever with the application's context
     * @return {@link IconRetriever}
     */
    public static IconRetriever getIconRetriever(){
        return new IconRetriever(AppPreferences.getApplicationContext());
    }

    /**
     * Gets the icon retriever with the specified context
     * @return {@link IconRetriever}
     */
    public static IconRetriever getIconRetriever(Context context){
        return new IconRetriever(context);
    }


    /**
     * App tools for icon retrieving
     */
    public static class IconRetriever implements Closeable{
        private boolean mIsClosed;
        private List<ResolveInfo> mAvailableActivities;
        private PackageManager mPackageManager;
        private IconFinder iconFinder;

        public IconRetriever(Context context){
            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            mPackageManager = context.getPackageManager();
            mAvailableActivities = mPackageManager.queryIntentActivities(i, 0);
            iconFinder = new IconFinder(context);
            mIsClosed = false;
        }

        /**
         * Retrieves the icon
         * @param packageName package name
         * @param appLabel label of the app
         * @return {@link Drawable}
         */
        public Drawable getAppIcon(String packageName, String appLabel){
            if(mIsClosed)
                throw new IllegalStateException("The IconRetriever was already closed!");
            ResolveInfo result = getResolveInfoForPackage(packageName, appLabel);
            if(result== null)
                return getAppDefaultIcon();
            return iconFinder.getFullResIcon(result);
        }

        /**
         * Retrieves the packages resolveInfo
         * @param packageName package name
         * @param appLabel label of the app to match the correct icon
         * @return {@link ResolveInfo} (can be null if not found)
         */
        private ResolveInfo getResolveInfoForPackage(String packageName, String appLabel){
            for (ResolveInfo ri: mAvailableActivities) {
                if (TextUtils.equals(ri.activityInfo.packageName, packageName) &&
                        TextUtils.equals(ri.loadLabel(mPackageManager), appLabel))
                    return ri;
            }
            return null;
        }

        @Override
        public void close(){
            mIsClosed = true;
            mAvailableActivities.clear();
            mAvailableActivities = null;
            mPackageManager = null;
            iconFinder = null;
        }
    }
}
