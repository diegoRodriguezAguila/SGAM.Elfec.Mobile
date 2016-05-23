package com.elfec.sgam.view.launcher;

import com.elfec.sgam.presenter.views.IMainView;

/**
 * Created by drodriguez on 23/05/2016.
 * Class which is meant to connect all the views with the launcher's functions
 */
public class LauncherMain {
    private static IMainView sMainView;

    public static IMainView getGetMainView(){
        if(sMainView==null)
            throw new IllegalStateException("Main view is null, it wasn't set, please use init to" +
                    " initialize it!");
        return sMainView;
    }

    /**
     * Initialize main view
     * @param view view
     */
    public static void init(IMainView view){
        sMainView = view;
    }
}
