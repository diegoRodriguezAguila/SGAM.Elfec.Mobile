package com.elfec.sgam.view.launcher.session;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.elfec.sgam.R;
import com.elfec.sgam.model.User;
import com.elfec.sgam.settings.AppPreferences;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Closeable;

/**
 * Notifies the current session's user
 */
public class SessionNotifier {

    private static final int SESSION_NOTIF = 42;

    private SessionNotifier() {
    }

    public static SessionNotifier create() {
        return new SessionNotifier();
    }

    /**
     * Sets the current user displayed in the session notification
     *
     * @param user user to display
     */
    public void setCurrentUser(User user) {
        if (user == null) {
            clearCurrentUser();
            return;
        }
        final Context context = AppPreferences.getApplicationContext();
        final NotificationManager manager = ((NotificationManager) (context.getSystemService
                (Context.NOTIFICATION_SERVICE)));
        RemoteViews notifView = getRemoteViews(user, context);
        final Notification notif = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.user)
                .setOngoing(true).setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setContent(notifView)
                .setColor(ContextCompat.getColor(context, R.color.color_primary))
                .build();
        notif.contentView = notifView;
        notif.bigContentView = notifView;
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener((picasso, uri, exception) -> exception.printStackTrace());
        builder.build().load(Uri.parse(user.getPhotoUrl().toString()))
                .into(new UserPhotoTarget(manager, notif));

        manager.notify(SESSION_NOTIF, notif);
    }

    public static class DownloadCancelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final View dialogView = LayoutInflater.from(context).inflate(
                    R.layout.session_status_layout, null, false);
            AlertDialog mDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style
                    .AppCustomTheme))
                    .setView(dialogView)
                    .setCancelable(true).create();
            mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mDialog.show();
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
            System.out.println("Received Cancelled Event");
        }
    }

    @NonNull
    private RemoteViews getRemoteViews(User user, Context context) {
        Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default);
        RemoteViews notifView = new RemoteViews(context.getPackageName(),
                R.layout.session_status_layout);
        notifView.setImageViewBitmap(R.id.img_user_photo, placeholder);
        notifView.setTextViewText(R.id.lbl_user_fullname, user.getFullName());
        notifView.setTextViewText(R.id.lbl_user_username, user.getUsername());
        Intent closeButton = new Intent("Download_Cancelled");
        closeButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, closeButton, 0);
        notifView.setOnClickPendingIntent(R.id.btn_user_options, pendingSwitchIntent);
        return notifView;
    }

    /**
     * Clears the current user notification
     */
    public void clearCurrentUser() {
        NotificationManager manager = ((NotificationManager) (AppPreferences
                .getApplicationContext().getSystemService
                (Context.NOTIFICATION_SERVICE)));
        manager.cancel(SESSION_NOTIF);
    }

    private class UserPhotoTarget implements Target, Closeable {
        private NotificationManager mManager;
        private Notification mNotification;
        private boolean mIsClosed;

        public UserPhotoTarget(NotificationManager manager, Notification notification) {
            this.mManager = manager;
            this.mNotification = notification;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if(mIsClosed)
                return;
            mNotification
                    .contentView.setImageViewBitmap(R.id.img_user_photo, bitmap);
            mManager.notify(SESSION_NOTIF, mNotification);
            this.close();
        }

        @Override
        public void close() {
            mIsClosed = true;
            mManager = null;
            mNotification = null;
        }

        //region Unnecessary methods
        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
        //endregion
    }
}
