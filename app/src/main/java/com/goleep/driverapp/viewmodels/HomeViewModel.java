package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.RequestConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Summary;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.DriverDataParser;
import com.goleep.driverapp.services.network.jsonparsers.SummaryParser;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.storage.LocalStorageService;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vishalm on 12/02/18.
 */

public class HomeViewModel extends AndroidViewModel {
    private Context context;

    private Summary summary = new Summary();
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
                        logoutCallback.onResponseReceived(null, false, null, false);
                        break;
                    case NetworkConstants.FAILURE:
                        logoutCallback.onResponseReceived(null, false, errorMessage, false);
                        break;
                    case NetworkConstants.NETWORK_ERROR:
                        logoutCallback.onResponseReceived(null, true, errorMessage, false);
                        break;
                    case NetworkConstants.UNAUTHORIZED:
                        logoutCallback.onResponseReceived(null, false, errorMessage, true);
                }
            }
        });
    }

    public void getDriverProfile(final UILevelNetworkCallback driverProfileCallback) {
        int driverId = LocalStorageService.sharedInstance().getLocalFileStore().getInt(context, SharedPreferenceKeys.DRIVER_ID);
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context,
                UrlConstants.DRIVERS_URL+"/"+driverId,
                true, new NetworkAPICallback() {
            @Override
            public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                switch (type){
                    case NetworkConstants.SUCCESS:
                        List<DriverEntity> driverEntities = new ArrayList<>();
                        DriverDataParser driverDataParser = new DriverDataParser();
                        DriverEntity driver =  driverDataParser.driverResponseByParsingJsonResponse(response);
                        if(driver != null){
                            driverEntities.add(driver);
                            RoomDBService.sharedInstance().getDatabase(context).driverDao().insertDriver(driver);
                        }
                        driverProfileCallback.onResponseReceived(driverEntities, false, null, false);
                        break;
                    case NetworkConstants.FAILURE:
                        driverProfileCallback.onResponseReceived(null, false, errorMessage, false);
                        break;
                    case NetworkConstants.NETWORK_ERROR:
                        driverProfileCallback.onResponseReceived(null, true, errorMessage, false);
                        break;
                    case NetworkConstants.UNAUTHORIZED:
                        driverProfileCallback.onResponseReceived(null, false, errorMessage, true);
                }
            }
        });
    }

    public void uploadProfileImage(File imageFile){
        String authToken = LocalStorageService.sharedInstance().getLocalFileStore().
                getString(getApplication().getApplicationContext(), SharedPreferenceKeys.AUTH_TOKEN);
        String userId = LocalStorageService.sharedInstance().getLocalFileStore().
                getString(getApplication().getApplicationContext(), SharedPreferenceKeys.USER_ID);
        NetworkService.sharedInstance().getNetworkClient().uploadImage(authToken, UrlConstants.UPDATE_PROFILE_IMAGE+userId,
                imageFile, "profile_image.jpg");
    }

    public Summary getSummary() {
        return summary;
    }

    public void getSummary(final UILevelNetworkCallback summaryCallback) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context, UrlConstants.SUMMARY_URL,
                true, new NetworkAPICallback() {
            @Override
            public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                switch (type){
                    case NetworkConstants.SUCCESS:
                        List<Summary> summaryList = new ArrayList<>();
                        SummaryParser summaryParser = new SummaryParser();
                        summary = summaryParser.summaryResponseByParsingJsonResponse(response);
                        if(summary != null){
                            summaryList.add(summary);
                        }
                        summaryCallback.onResponseReceived(summaryList, false, null, false);
                        break;
                    case NetworkConstants.FAILURE:
                        summaryCallback.onResponseReceived(null, false, errorMessage, false);
                        break;
                    case NetworkConstants.NETWORK_ERROR:
                        summaryCallback.onResponseReceived(null, true, errorMessage, false);
                        break;
                    case NetworkConstants.UNAUTHORIZED:
                        summaryCallback.onResponseReceived(null, false, errorMessage, true);
                }
            }
        });
    }
}
