package com.tracotech.tracoman.helpers.uihelpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.tracotech.tracoman.R;

public class NotificationHelper {

    private Context context;
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendNotification(String title, String messageBody, PendingIntent pendingIntent) {
        if (notificationManager == null) return;
        String channelId = context.getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder = buildNotification(channelId, R.drawable.ic_notification_icon, title, messageBody, pendingIntent);
        createNotificationChannel(channelId, context.getString(R.string.app_name));
        notificationManager.notify(0, notificationBuilder.build());
    }

    private NotificationCompat.Builder buildNotification(String channelId, int icon, String title, String messageBody, PendingIntent pendingIntent) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
    }

    private void createNotificationChannel(String channelId, String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
