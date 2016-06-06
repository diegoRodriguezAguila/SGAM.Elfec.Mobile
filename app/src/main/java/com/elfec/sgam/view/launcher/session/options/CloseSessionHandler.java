package com.elfec.sgam.view.launcher.session.options;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.ui.AppCompatAlertDialogUtils;
import com.elfec.sgam.view.launcher.LauncherMain;

/**
 * Created by drodriguez on 19/05/2016.
 * Handler for close session option
 */
public class CloseSessionHandler implements IOptionHandler {
    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.ses_opt_close_session)
                .setIcon(R.drawable.close_session)
                .setMessage(R.string.msg_confirm_close_session)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_ok, (d, which) -> LauncherMain.getGetMainView()
                        .closeSession())
                .setCancelable(true).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        AppCompatAlertDialogUtils.setTitleFont(dialog);
    }
}
