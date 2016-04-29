package com.elfec.sgam.view.launcher;

import com.elfec.sgam.model.AppDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for managin launcher apps
 */
public class LauncherApps {
    /**
     * Unique instance
     */
    private static LauncherApps sInstance;
    /**
     * Apps caché
     */
    private List<AppDetail> mApplications;

    public static LauncherApps instance(){
        if(sInstance==null)
            sInstance = new LauncherApps();
        return sInstance;
    }

    private LauncherApps(){
        mApplications = new ArrayList<>();
    }

    /**
     * Asigns the app caché
     * @param applications apps
     */
    public void setAppsCache(List<AppDetail> applications){
        mApplications = applications;
    }

    /**
     * Gets the apps caché
     * @return app list
     */
    public List<AppDetail> getAppsCache(){
        return mApplications;
    }

    /**
     * Clears the apps caché
     */
    public void clearAppsCache(){
        if (mApplications != null)
            mApplications.clear();
    }
}
