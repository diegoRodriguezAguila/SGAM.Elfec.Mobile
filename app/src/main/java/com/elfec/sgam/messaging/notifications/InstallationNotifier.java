package com.elfec.sgam.messaging.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.elfec.sgam.R;
import com.elfec.sgam.business_logic.ApplicationManager.ApkDownloadListener;
import com.elfec.sgam.helpers.utils.InstallationUtils;
import com.elfec.sgam.model.Installation;
import com.elfec.sgam.settings.AppPreferences;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Closeable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by drodriguez on 08/06/2016.
 * Notifies about pending installations
 */
public class InstallationNotifier {
    public static Map<String, Installation> sPendingInstallations;

    static {
        sPendingInstallations = new HashMap<>();
    }

    /**
     * @param installationId id of the {@link Installation}
     */
    public static void setInstallationDone(@NonNull String installationId) {
        Installation pendingIns = sPendingInstallations.get(installationId);
        if (pendingIns == null)
            return;
        final Context context = AppPreferences.getApplicationContext();
        final NotificationManager manager = ((NotificationManager) (context.getSystemService
                (Context.NOTIFICATION_SERVICE)));
        manager.cancel(pendingIns.getId().hashCode());
        sPendingInstallations.remove(installationId);
    }

    /**
     * Shows a pending Installation download as notification
     *
     * @param installation pending {@link Installation}
     */
    public static ApkDownloadListener showApplicationDownload(@NonNull Installation installation) {
        sPendingInstallations.put(installation.getId(), installation);
        final Context context = AppPreferences.getApplicationContext();
        final NotificationManager manager = ((NotificationManager) (context.getSystemService
                (Context.NOTIFICATION_SERVICE)));
        final Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.window);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notif_logo)
                .setLargeIcon(placeholder)
                .setOngoing(true).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                .setContentTitle(installation.getVersionedAppName())
                .setContentText(context.getString(R.string.msg_starting_download))
                .setProgress(0, 0, true)
                .setColor(ContextCompat.getColor(context, R.color.color_primary));

        startImageRequest(installation, context, manager, builder);
        manager.notify(installation.getId().hashCode(), builder.build());
        return new NotificationApkDownloadListener(builder, manager,
                installation.getId().hashCode());
    }

    /**
     *
     * @param installation installation
     */
    public static void showApplicationUninstall(@NonNull Installation installation) {
        sPendingInstallations.put(installation.getId(), installation);
        final Context context = AppPreferences.getApplicationContext();
        final NotificationManager manager = ((NotificationManager) (context.getSystemService
                (Context.NOTIFICATION_SERVICE)));
        final int notifId = installation.getId().hashCode();
        final Bitmap placeholder = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.window);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notif_logo)
                .setLargeIcon(placeholder)
                .setOngoing(true).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setContentTitle(installation.getVersionedAppName())
                .setContentText(context.getString(R.string.msg_uninstall_pending))
                .setContentIntent(PendingIntent.getActivity(context, notifId,
                        InstallationUtils.appUninstallationIntent(installation.getPackageName()), 0))
                .setColor(ContextCompat.getColor(context, R.color.color_primary));

        startImageRequest(installation, context, manager, builder);
        manager.notify(notifId, builder.build());
    }

    /**
     * Starts the app icon request with picasso in the main handler
     *
     * @param installation mInstallation
     * @param context      context
     * @param manager      mManager
     * @param builder      mBuilder
     */
    private static void startImageRequest(Installation installation, Context context,
                                          NotificationManager manager,
                                          NotificationCompat.Builder builder) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Picasso.Builder picassoBuilder = new Picasso.Builder(context);
            picassoBuilder.listener((picasso, uri, exception) -> exception.printStackTrace());
            picassoBuilder.build().load(Uri.parse(installation.getIconUrl().toString()))
                    .into(new AppIconTarget(manager, builder, installation.getId().hashCode()));
        });
    }

    private static class AppIconTarget implements Target, Closeable {
        private NotificationManager mManager;
        private NotificationCompat.Builder mBuilder;
        private int mNotificationId;
        private boolean mIsClosed;

        public AppIconTarget(NotificationManager manager,
                             NotificationCompat.Builder notification,
                             int notificationId) {
            this.mManager = manager;
            this.mBuilder = notification;
            this.mNotificationId = notificationId;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if (mIsClosed)
                return;
            mBuilder.setLargeIcon(bitmap);
            mManager.notify(mNotificationId, mBuilder.build());
            this.close();
        }

        @Override
        public void close() {
            mIsClosed = true;
            mManager = null;
            mBuilder = null;
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

    /**
     * Listener for the notification in a download operation
     */
    public static class NotificationApkDownloadListener extends ApkDownloadListener
            implements Closeable {
        private NotificationCompat.Builder mBuilder;
        private NotificationManager mManager;
        private int mNotificationId;
        private long mFileSize;
        private String mMsgProgress;

        public NotificationApkDownloadListener(NotificationCompat.Builder builder,
                                               NotificationManager manager,
                                               int notificationId) {
            this.mBuilder = builder;
            this.mManager = manager;
            this.mNotificationId = notificationId;
            this.mMsgProgress = builder.mContext.getString(R.string.msg_download_progress);
        }

        @Override
        public void onProgress(short percentage, long downloadedSoFar) {
            mBuilder.setContentText(String.format(mMsgProgress,
                    downloadedSoFar / 1024, mFileSize)).setProgress(100, percentage, false);
            mManager.notify(mNotificationId, mBuilder.build());
        }

        @Override
        public void onStarted(long fileSize) {
            this.mFileSize = fileSize / 1024; // to Kb
            mBuilder.setContentText(String.format(mMsgProgress, 0,
                    this.mFileSize)).setProgress(100, 0, false);
            mManager.notify(mNotificationId, mBuilder.build());
        }

        @Override
        public void onCompleted(File apkFile) {
            mBuilder.setContentText(mBuilder.mContext.getString(R.string.msg_download_completed))
                    .setProgress(0, 0, false)
                    .setContentIntent(PendingIntent.getActivity(mBuilder.mContext, mNotificationId,
                            InstallationUtils.appInstallationIntent(apkFile), 0));
            mManager.notify(mNotificationId, mBuilder.build());
            close();
        }

        @Override
        public void close() {
            mManager = null;
            mBuilder = null;
        }
    }
}
