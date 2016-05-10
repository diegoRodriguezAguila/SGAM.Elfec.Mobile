package com.elfec.sgam;

import android.app.Application;

import com.elfec.sgam.messaging.GcmNotificationHandler;
import com.elfec.sgam.messaging.GcmNotificationReceiver;
import com.elfec.sgam.settings.AppPreferences;

import net.danlew.android.joda.JodaTimeAndroid;

import io.paperdb.Paper;
import rx_gcm.internal.RxGcm;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Aplicación que extiende de la aplicación android
 */
public class ElfecApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/segoe_ui.ttf").setFontAttrId(R.attr.fontPath).build());
        JodaTimeAndroid.init(this);
        AppPreferences.init(this);
        Paper.init(this);
        RxGcm.Notifications.register(this, GcmNotificationHandler.class,
                GcmNotificationReceiver.class).subscribe();
    }

}
