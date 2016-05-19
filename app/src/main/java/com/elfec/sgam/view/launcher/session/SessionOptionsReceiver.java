package com.elfec.sgam.view.launcher.session;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.elfec.sgam.R;
import com.elfec.sgam.helpers.ui.ContextUtils;
import com.elfec.sgam.view.adapter.listview.SessionOptionAdapter;

import static com.elfec.sgam.view.adapter.listview.SessionOptionAdapter.*;

/**
 * Receiver for the options button on user notification
 */
public class SessionOptionsReceiver extends BroadcastReceiver {
    private static boolean mAreOptionsShown;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mAreOptionsShown){
            closeNotificationsPanel(context);
            return;
        }
        mAreOptionsShown = true;
        context = ContextUtils.wrapContext(context);
        ListView listView = getDialogOptionsView(context);
        TextView title = getDialogTitleView(context);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCustomTitle(title).setView(listView)
                .setNegativeButton(R.string.btn_cancel, null)
                .setOnDismissListener(d -> mAreOptionsShown = false)
                .setCancelable(true).create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            SessionOption opt = (SessionOption) listView
                    .getAdapter().getItem(position);
            dialog.dismiss();
        });
        closeNotificationsPanel(context);
    }

    @NonNull
    private TextView getDialogTitleView(Context context) {
        TextView title = (TextView) LayoutInflater.from(context).inflate(R.layout.title_view, null, false);
        title.setText(R.string.title_session_options);
        title.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable
                .session_options_d), null, null, null);
        return title;
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