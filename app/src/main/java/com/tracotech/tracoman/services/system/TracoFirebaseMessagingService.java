package com.tracotech.tracoman.services.system;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tracotech.tracoman.R;
import com.tracotech.tracoman.leep.pickup.pickup.PickupWarehouseActivity;
import com.tracotech.tracoman.utils.LogUtils;

public class TracoFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "TracoFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtils.debug(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            LogUtils.debug(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            LogUtils.debug(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        handleMessage(remoteMessage);
    }

    private void handleMessage(RemoteMessage remoteMessage) {
        sendNotification(getString(R.string.app_name), "New Order Assigned");
    }

    private void sendNotification(String title, String messageBody) {
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_history) // Update icon
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(getPendingIntent());

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, PickupWarehouseActivity.class);
        return PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
