package com.elfec.sgam;

import android.app.Application;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.cesarferreira.rxpaper.RxPaper;
import com.elfec.sgam.helpers.alarm.AlarmHelper;
import com.elfec.sgam.helpers.ui.AppCompatAlertDialogUtils;
import com.elfec.sgam.helpers.ui.ContextUtils;
import com.elfec.sgam.messaging.GcmNotificationBgReceiver;
import com.elfec.sgam.messaging.GcmNotificationReceiver;
import com.elfec.sgam.settings.AppPreferences;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer;
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
        RxPaper.init(this);
        Paper.addSerializer(DateTime.class, new JodaDateTimeSerializer());
        RxGcm.Notifications.register(this, GcmNotificationReceiver.class,
                GcmNotificationBgReceiver.class).subscribe();
        setUnhandledErrorsReset();
    }

    /**
     * Reinicia el launcher ante cualquier error inesperado
     */
    private void setUnhandledErrorsReset() {
        Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> {
            ex.printStackTrace();
            showErrorDialog();
        });
    }

    /**
     * Shows the error dialog
     */
    private void showErrorDialog() {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                AlertDialog dialog = new AlertDialog.Builder(
                        ContextUtils.wrapContext(getApplicationContext()),
                        R.style.AppCustomTheme_AlertDialog_Dark).setTitle(R.string.title_unexpected_error)
                        .setMessage(R.string.msg_unexpected_error)
                        .setCancelable(false)
                        .setIcon(R.drawable.error_dialog)
                        .setPositiveButton(R.string.btn_ok, (d, w) -> {
                            AlarmHelper.scheduleAppRestart(50);
                            System.exit(2);
                        }).create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();
                AppCompatAlertDialogUtils.setTitleFont(dialog);
                Looper.loop();

            }
        }.start();
    }

}
