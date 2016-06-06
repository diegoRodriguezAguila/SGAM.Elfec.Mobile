package com.elfec.sgam.view.launcher.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.ListView;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.ui.AppCompatAlertDialogUtils;
import com.elfec.sgam.helpers.ui.ContextUtils;
import com.elfec.sgam.settings.AppPreferences;
import com.elfec.sgam.view.adapter.listview.SessionOptionAdapter;
import com.elfec.sgam.view.launcher.LauncherMain;
import com.elfec.sgam.view.launcher.session.options.IOptionHandler;

/**
 * Receiver for the options button on user notification
 */
public class SessionOptionsReceiver extends BroadcastReceiver {
    private static boolean mAreOptionsShown;

    @Override
    public void onReceive(Context cont, Intent intent) {
        if (mAreOptionsShown || LauncherMain.getGetMainView().isClosingSession()){
            closeNotificationsPanel(cont);
            return;
        }
        mAreOptionsShown = true;
        final Context context =
                ContextUtils.wrapContext(AppPreferences.getApplicationContext());
        ListView listView = getDialogOptionsView(context);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.title_session_options)
                .setIcon(R.drawable.session_options_d)
                .setView(listView)
                .setNegativeButton(R.string.btn_cancel, null)
                .setOnDismissListener(d -> mAreOptionsShown = false)
                .setCancelable(true).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        AppCompatAlertDialogUtils.setTitleFont(dialog);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            SessionOption opt = (SessionOption) listView
                    .getAdapter().getItem(position);
            IOptionHandler handler = opt.getHandler();
            if(handler!=null)
                handler.handle(context);
            dialog.dismiss();
        });
        closeNotificationsPanel(cont);
    }

    /**
     * Sends an intent to request closing of the notifications panel
     * @param context context
     */
    private void closeNotificationsPanel(Context context){
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    @NonNull
    private ListView getDialogOptionsView(Context context) {
        ListView listView = new ListView(context);
        int margin = (int) context.getResources().getDimension(R.dimen.margin_options_menu);
        listView.setPadding(margin, margin, margin, 0);
        listView.setAdapter(new SessionOptionAdapter(context, R.layout.session_option_item));
        return listView;
    }

}