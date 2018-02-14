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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vishalm on 08/02/18.
 */

public class LoginViewModel extends AndroidViewModel {
    private Context context;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void login(String phoneNumber, String password, String code, final UILevelNetworkCallback loginCallBack) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put(RequestConstants.KEY_PHONE_NUMBER, phoneNumber);
        bodyParams.put(RequestConstants.KEY_COUNTRY_CODE, code);
        bodyParams.put(RequestConstants.KEY_PASSWORD, password);
        NetworkService.sharedInstance().getNetworkClient().makeJsonPostRequest(context, UrlConstants.LOGIN_URL,
                false, bodyParams, new NetworkAPICallback() {
            @Override
            public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                switch (type){
                    case NetworkConstants.SUCCESS:
                        try{
                            LocalStorageService.sharedInstance().getLocalFileStore().store(context,
                                    SharedPreferenceKeys.DRIVER_ID, ((JSONObject)response.get(0)).getString("id"));
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }
                        loginCallBack.onResponseReceived(null, false, null);
                        break;
                    case NetworkConstants.FAILURE:
                        loginCallBack.onResponseReceived(null, false, errorMessage);
                        break;
                    case NetworkConstants.NETWORK_ERROR:
                        loginCallBack.onResponseReceived(null, true, errorMessage);
                        break;
                }
            }
        });
    }
}
