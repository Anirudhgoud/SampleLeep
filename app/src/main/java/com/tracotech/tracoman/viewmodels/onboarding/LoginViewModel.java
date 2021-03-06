package com.tracotech.tracoman.viewmodels.onboarding;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.RequestConstants;
import com.tracotech.tracoman.constants.SharedPreferenceKeys;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.helpers.uimodels.Country;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.services.storage.LocalStorageService;
import com.google.gson.Gson;
import com.tracotech.tracoman.utils.LogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vishalm on 08/02/18.
 */

public class LoginViewModel extends AndroidViewModel {

    private List<Country> countries = new ArrayList<>();
    private Country selectedCountry;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public void login(String phoneNumber, String password, final String code, final UILevelNetworkCallback loginCallBack) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put(RequestConstants.KEY_PHONE_NUMBER, phoneNumber);
        bodyParams.put(RequestConstants.KEY_COUNTRY_CODE, code);
        bodyParams.put(RequestConstants.KEY_PASSWORD, password);
        Context context = getApplication().getApplicationContext();
        NetworkService.sharedInstance().getNetworkClient().makeJsonPostRequest(context, UrlConstants.LOGIN_URL,
                false, bodyParams, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            JSONObject userObj = (JSONObject) response.opt(0);
                            storeUserMeta(userObj);
                            loginCallBack.onResponseReceived(null, false, null, false);
                            break;
                        case NetworkConstants.FAILURE:
                            loginCallBack.onResponseReceived(null, false, errorMessage, false);
                            break;
                        case NetworkConstants.NETWORK_ERROR:
                            loginCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;
                        case NetworkConstants.UNAUTHORIZED:
                            loginCallBack.onResponseReceived(null, false, errorMessage, true);
                    }
                });
    }

    private void storeUserMeta(JSONObject userObj) {
        Context context = getApplication().getApplicationContext();
        LocalStorageService.sharedInstance().getLocalFileStore().store(context,
                SharedPreferenceKeys.PROFILE_URL, userObj.optString("profile_image_url"));
        LocalStorageService.sharedInstance().getLocalFileStore().store(context,
                SharedPreferenceKeys.USER_ID, userObj.optString("id"));
        LocalStorageService.sharedInstance().getLocalFileStore().store(context, SharedPreferenceKeys.SELECTED_COUNTRY,
                new Gson().toJson(selectedCountry));
        JSONObject driver = userObj.optJSONObject("driver");
        if (driver != null) {
            int driverId = driver.optInt("id");
            LocalStorageService.sharedInstance().getLocalFileStore().store(context,
                    SharedPreferenceKeys.DRIVER_ID, driverId);
        }
        JSONObject currency = userObj.optJSONObject("currency");
        if (currency != null) {
            String symbol = currency.optString("symbol");
            LocalStorageService.sharedInstance().getLocalFileStore().store(context, SharedPreferenceKeys.CURRENCY_SYMBOL, symbol);
        }
    }

    public void sendFCMTokenToServer() {
        String token = getFCMToken();
        if (token == null) return;
        LogUtils.error("", "fcm token: " + token);
        NetworkService.sharedInstance().getNetworkClient().makeJsonPutRequest(getApplication(), UrlConstants.UPDATE_FCM_TOKEN, true, Collections.singletonMap("fcm_token", token), (type, response, errorMessage) -> {
        });
    }

    private String getFCMToken() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.error("", "Refreshed token: " + fcmToken);
        return fcmToken;
    }
}
