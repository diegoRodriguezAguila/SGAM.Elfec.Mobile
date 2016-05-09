package com.elfec.sgam.security.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.utils.TaskHelper;
import com.elfec.sgam.view.launcher.ApplicationTools;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Servicio para deteccion de ejecución de aplicaciones
 */
public class PackagePermissionService extends AccessibilityService {
    /**
     * The app just received by the event package
     */
    private String lastEventPackage;

    private List<String> essentialApps = new ArrayList<>();

    @Override
    public void onCreate(){
        super.onCreate();
        essentialApps.add("com.elfec.sgam");
        essentialApps.add("com.android.systemui");
        essentialApps.add("com.android.settings");
        essentialApps.add("com.android.keyguard");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        String packageName = event.getPackageName().toString();
        if(!packageName.equals(lastEventPackage)) {
            lastEventPackage = packageName;
            if(!essentialApps.contains(packageName)){
                showAppLockDialog(packageName);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Muestra un dialogo de prohibición de uso de aplicación
     * @param packageName paquete
     */
    private void showAppLockDialog(final String packageName) {
        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(
                CalligraphyContextWrapper.wrap(this),
                R.style.AppCustomTheme))
                .setTitle(R.string.app_locked_title)
                .setMessage(getAppLockMessage(packageName))
                .setPositiveButton(R.string.btn_ok,(dg, which) -> {
                            new Handler(getMainLooper())
                                    .postDelayed(()->finishActivity(packageName), 80);
                }).setCancelable(false).setIcon(R.drawable.error_dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private CharSequence getAppLockMessage(String packageName){
        return Html.fromHtml(getString(R.string.app_locked_msg, ApplicationTools.getAppLabel
                (packageName, this)));
    }

    /**
     * Finishes the activity
     * @param packageName package name of the activity
     * @return true if it was finished
     */
    public boolean finishActivity(final String packageName){
        int count = 0;
        String lastShownPackage = packageName;
        while(TextUtils.equals(packageName, lastShownPackage) ||
                !lastShownPackage.equals("com.elfec.sgam")){
            count++;
            performGlobalAction(GLOBAL_ACTION_BACK);
            if(count == 5){
                performGlobalAction(GLOBAL_ACTION_HOME);
                return true;
            }
            try {
                Thread.sleep(180);
            } catch (InterruptedException e) {
                return false;
            }
            lastShownPackage = TaskHelper.getCurrentActivity();
        }
        return true;
    }

    @Override
    public void onInterrupt() {

    }
}
