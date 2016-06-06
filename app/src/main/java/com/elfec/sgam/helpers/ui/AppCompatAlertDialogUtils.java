package com.elfec.sgam.helpers.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

/**
 * Created by drodriguez on 06/06/2016.
 * Utils for AppCompat's AlertDialog
 */
public class AppCompatAlertDialogUtils {
    /**
     * Sets the font for the dialog title. Useful when inflating dialogs
     * with a non Calligraphy Context
     * @param dialog dialog
     */
    public static void setTitleFont(@NonNull AlertDialog dialog){
        TextView txtTitle = (TextView) dialog.findViewById(android.support.v7.appcompat.R.id
                .alertTitle);
        CalligraphyUtils.applyFontToTextView(dialog.getContext(), txtTitle,
                "fonts/segoe_ui_semilight.ttf");
    }
}
