package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.RequestConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.UserMeta;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vishalm on 12/02/18.
 */

public class HomeViewModel extends AndroidViewModel {
    private Context context;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void signout(final UILevelNetworkCallback logoutCallback) {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(RequestConstants.AUTHORIZATION, LocalStorageService.sharedInstance()
                .getLocalFileStore().getString(context, SharedPreferenceKeys.AUTH_TOKEN));
        NetworkService.sharedInstance().getNetworkClient().makeDeleteRequest(context, UrlConstants.LOGOUT_URL,
                true, headerParams, new NetworkAPICallback() {
            @Override
            public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                switch (type){
                    case NetworkConstants.SUCCESS:
                        logoutCallback.onResponseReceived(null, false, null);
                        break;
                    case NetworkConstants.FAILURE:
                        logoutCallback.onResponseReceived(null, false, errorMessage);
                        break;
                    case NetworkConstants.NETWORK_ERROR:
                        logoutCallback.onResponseReceived(null, true, errorMessage);
                        break;
                }
            }
        });
    }

    public void getDriverProfile(UILevelNetworkCallback driverProfileCallback) {

    }
}
