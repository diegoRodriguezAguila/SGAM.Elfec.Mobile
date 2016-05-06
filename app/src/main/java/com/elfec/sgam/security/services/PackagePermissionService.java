package com.elfec.sgam.security.services;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.elfec.sgam.helpers.utils.TaskHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para deteccion de ejecución de aplicaciones
 */
public class PackagePermissionService extends AccessibilityService {
    /**
     * The app just received by the event package
     */
    private String lastEventPackage;

    private List<String> permitted = new ArrayList<>();

    @Override
    public void onCreate(){
        super.onCreate();
        permitted.add("com.elfec.sgam");
        permitted.add("com.android.systemui");
        permitted.add("com.android.settings");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        String packageName = event.getPackageName().toString();
        if(!packageName.equals(lastEventPackage)) {
            lastEventPackage = packageName;
            Toast.makeText(this, String.format("SE ABRIO LA ACTIVITY: %s", packageName), Toast
                    .LENGTH_SHORT).show();
            if(!permitted.contains(packageName)){
                new Handler(getMainLooper()).postDelayed(()->{
                    finishActivity(packageName);
                    Toast.makeText(this, String.format("KILL ON: %s", packageName),
                            Toast
                            .LENGTH_SHORT).show();
                }, 1000);
            }
        }
    }

    /**
     * Finishes the activity
     * @param packageName package name of the activity
     * @return true if it was finished
     */
    public boolean finishActivity(final String packageName){
        int count = 0;
        String lastShownPackage = packageName;
        while(TextUtils.equals(packageName, lastShownPackage)){
            count++;
            performGlobalAction(GLOBAL_ACTION_BACK);
            if(count == 5){
                performGlobalAction(GLOBAL_ACTION_HOME);
                return true;
            }
            try {
                Thread.sleep(200);
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
