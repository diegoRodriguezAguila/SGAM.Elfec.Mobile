package com.elfec.sgam.security.services;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * Servicio para deteccion de ejecución de aplicaciones
 */
public class PackagePermissionService extends AccessibilityService {
    private static String lastPackage;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName().toString();
        if(!packageName.equals(lastPackage)) {
            lastPackage = packageName;
            Toast.makeText(this, String.format("SE ABRIO LA ACTIVITY: %s", packageName), Toast
                    .LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInterrupt() {

    }
}
