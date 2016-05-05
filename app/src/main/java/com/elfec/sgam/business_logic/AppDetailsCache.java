package com.elfec.sgam.business_logic;

import com.elfec.sgam.local_storage.AppDetailsDataStorage;
import com.elfec.sgam.model.AppDetail;

import java.util.List;

import rx.Observable;

/**
 * Clase encargada de cachear las aplicaciones del launcher
 */
public class AppDetailsCache {
    public static Observable<List<AppDetail>> saveApplicationsCache(List<AppDetail> apps){
        return new AppDetailsDataStorage().saveAppsCache(apps);
    }
    public static Observable<List<AppDetail>> getApplicationsCache(){
        return new AppDetailsDataStorage().getAppsCache();
    }
}
