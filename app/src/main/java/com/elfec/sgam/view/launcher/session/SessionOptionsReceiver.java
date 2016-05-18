package com.elfec.sgam.view.launcher.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.elfec.sgam.R;

/**
 * Receiver for the options button on user notification
 */
public class SessionOptionsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final View dialogView = LayoutInflater.from(context).inflate(
                R.layout.session_status_layout, null, false);
        AlertDialog mDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style
                .AppCustomTheme)).setView(dialogView)
                .setCancelable(true).create();
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mDialog.show();
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
        System.out.println("Received Cancelled Event");
    }
}