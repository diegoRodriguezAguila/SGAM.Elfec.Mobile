package com.elfec.sgam.view.launcher.session;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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

    private static User sCurrentUser;

    private static final int SESSION_NOTIF = 42;

    private SessionNotifier() {
    }

    public static SessionNotifier create() {
        return new SessionNotifier();
    }

    /**
     * Gets the last current user set by any instance of {@link SessionNotifier}
     * via {@link SessionNotifier#setCurrentUser(User)}
     * @return last current user set, null if cleared or not set
     */
    public static @Nullable User getCurrentUser(){
        return sCurrentUser;
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
        sCurrentUser = user;
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

    @NonNull
    private RemoteViews getRemoteViews(User user, Context context) {
        Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_default);
        RemoteViews notifView = new RemoteViews(context.getPackageName(),
                R.layout.session_status_layout);
        notifView.setImageViewBitmap(R.id.img_user_photo, placeholder);
        notifView.setTextViewText(R.id.lbl_user_fullname, user.getFullName());
        notifView.setTextViewText(R.id.lbl_user_username, user.getUsername());
        Intent closeButton = new Intent("options_pressed");
        closeButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, closeButton, 0);
        notifView.setOnClickPendingIntent(R.id.btn_user_options, pendingSwitchIntent);
        return notifView;
    }

    /**
     * Clears the current user notification
     */
    public void clearCurrentUser() {
        sCurrentUser = null;
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
