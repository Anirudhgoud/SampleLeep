package com.tracotech.tracoman.services.system;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.utils.LogUtils;

import java.util.Collections;

public class TracoFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "TracoFirebaseInstanceIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.error(TAG, "Refreshed token: " + refreshedToken);
        if (refreshedToken == null) return;
        sendFCMTokenToServer(refreshedToken);
    }

    private void sendFCMTokenToServer(String token) {
        NetworkService.sharedInstance().getNetworkClient().makeJsonPutRequest(getApplicationContext(), UrlConstants.UPDATE_FCM_TOKEN, true, Collections.singletonMap("fcm_token", token), (type, response, errorMessage) -> {
        });
    }
}
