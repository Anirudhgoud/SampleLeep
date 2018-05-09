package com.tracotech.tracoman.services.system;

import android.app.PendingIntent;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.NotificationType;
import com.tracotech.tracoman.helpers.uihelpers.NotificationHelper;
import com.tracotech.tracoman.leep.onboarding.SplashActivity;
import com.tracotech.tracoman.leep.pickup.pickup.PickupWarehouseActivity;
import com.tracotech.tracoman.utils.LogUtils;

import java.util.Map;

public class TracoFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "TracoFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null || remoteMessage.getData() == null) return;
        LogUtils.debug(TAG, "From: " + remoteMessage.getFrom());
        handleMessage(remoteMessage);
    }

    private void handleMessage(RemoteMessage remoteMessage) {
        Map<String, String> notificationData = remoteMessage.getData();
        if (notificationData.size() > 0) {
            LogUtils.error(TAG, "Message data payload: " + remoteMessage.getData());
            String type = notificationData.get("notification_type");
            if (type == null || type.isEmpty()) return;
            String body = notificationData.get("body");
            if (body == null || body.isEmpty())
                body = getString(R.string.default_notification_message);
            String title = notificationData.get("title");
            if (title == null || title.isEmpty())
                title = getString(R.string.default_notification_title);

            switch (type) {
                case NotificationType.DO_ASSIGNED:
                    new NotificationHelper().sendNotification(this, title, body, getPendingIntent(type));
                    break;
            }
        }
    }

    private PendingIntent getPendingIntent(String type) {
        Class intentClass;
        switch (type) {
            case NotificationType.DO_ASSIGNED:
                intentClass = PickupWarehouseActivity.class;
                break;
            default:
                intentClass = SplashActivity.class;
                break;
        }
        Intent intent = new Intent(this, intentClass);
        return PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
