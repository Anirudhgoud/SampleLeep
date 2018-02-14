package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.RequestConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.helpers.uimodels.UserMeta;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.ForgotPasswordActivity;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.goleep.driverapp.constants.UrlConstants.FORGOT_PASSWORD_URL;

/**
 * Created by vishalm on 14/02/18.
 */

public class ForgotPasswordViewModel extends AndroidViewModel {
    private Context context;
    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

    public void submitEmail(String email, final UILevelNetworkCallback submitEmailCallback) {
        Map<String, Object> bodyParams = new HashMap<>();
        JSONObject emailJson = new JSONObject();
        try {
            emailJson.put(RequestConstants.KEY_EMAIL, email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bodyParams.put(RequestConstants.KEY_USER, emailJson);
        NetworkService.sharedInstance().getNetworkClient().makeJsonPostRequest(context, FORGOT_PASSWORD_URL,
                false, bodyParams, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                submitEmailCallback.onResponseReceived(null, false, null);
                                break;
                            case NetworkConstants.FAILURE:
                                submitEmailCallback.onResponseReceived(null, false, errorMessage);
                                break;
                            case NetworkConstants.NETWORK_ERROR:
                                submitEmailCallback.onResponseReceived(null, true, errorMessage);
                                break;
                        }
                    }
                });
    }
}
