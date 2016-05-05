package com.elfec.sgam.local_storage;

import com.elfec.sgam.model.AppDetail;

import java.util.List;

import rx.Observable;

/**
 * Storage layer
 */
public class AppDetailsDataStorage {
    public static final String APP_DETAIL_BOOK = "app_detail.book";
    private static final String KEY = "apps_cache";
    private RxPaper mBook;

    public AppDetailsDataStorage(){
        mBook = RxPaper.book(APP_DETAIL_BOOK);
    }

    /**
     * Saves the policy rules of the specific type that applies to the user.
     * All non matching policy types are not going to be saved
     * @return observable with a list of rules
     */
    public Observable<List<AppDetail>> saveAppsCache(List<AppDetail> apps) {
        return mBook.write(KEY, apps);
    }

    /**
     * Retrives the apps caché
     * @return apps caché
     */
    public Observable<List<AppDetail>> getAppsCache() {
        return mBook.read(KEY);
    }
}
