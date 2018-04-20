package com.goleep.driverapp.viewmodels.main;

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
import com.goleep.driverapp.services.network.jsonparsers.StockProductParser;
import com.goleep.driverapp.services.network.jsonparsers.SummaryParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;
import com.goleep.driverapp.services.storage.LocalStorageService;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vishalm on 12/02/18.
 */

public class HomeViewModel extends AndroidViewModel {

    private AppDatabase leepDatabase;
    private Summary summary = new Summary();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public void signout(final UILevelNetworkCallback logoutCallback) {
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(RequestConstants.AUTHORIZATION, LocalStorageService.sharedInstance()
                .getLocalFileStore().getString(getApplication(), SharedPreferenceKeys.AUTH_TOKEN));
        NetworkService.sharedInstance().getNetworkClient().makeDeleteRequest(getApplication(), UrlConstants.LOGOUT_URL,
                true, headerParams, (type, response, errorMessage) -> {
                    logoutCallback.onResponseReceived(null, false, null, true);
                });
    }

    public void getDriverProfile(final UILevelNetworkCallback driverProfileCallback) {

        int driverId = LocalStorageService.sharedInstance().getLocalFileStore().getInt(getApplication(),
                SharedPreferenceKeys.DRIVER_ID);
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(),
                UrlConstants.DRIVERS_URL+"/"+driverId, true, (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            List<DriverEntity> driverEntities = new ArrayList<>();
                            DriverDataParser driverDataParser = new DriverDataParser();
                            DriverEntity driver =  driverDataParser.driverResponseByParsingJsonResponse(response);
                            if(driver != null){
                                driverEntities.add(driver);
                                leepDatabase.driverDao().insertDriver(driver);
                            }
                            List<WarehouseEntity> warehouseEntities = driverDataParser.warehouseByParsingResponse(response);
                            if(warehouseEntities != null) {
                                leepDatabase.warehouseDao().updateWarehouseDetails(warehouseEntities);
                            }
                            driverProfileCallback.onResponseReceived(driverEntities, false, null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            driverProfileCallback.onResponseReceived(null, false, errorMessage, false);
                            break;
                        case NetworkConstants.UNAUTHORIZED:
                            driverProfileCallback.onResponseReceived(null, false, errorMessage, true);


                            }
                        });
    }

    public void getStocks() {
        int driverId = LocalStorageService.sharedInstance().getLocalFileStore().getInt(
                getApplication().getApplicationContext(), SharedPreferenceKeys.DRIVER_ID);
        String url = UrlConstants.INVENTORIES_URL + "?drivers=" + driverId;
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(
                getApplication().getApplicationContext(), url, true,
                (type, response, errorMessage) -> {
                    System.out.print("");
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            StockProductParser parser = new StockProductParser();
                            List<StockProductEntity> stockProductEntities = parser.getStockProduct(response);
                            RoomDBService.sharedInstance().getDatabase(getApplication().
                                    getApplicationContext()).stockProductDao().updateAllDeliveryOrders(stockProductEntities);
                    }
                });
    }

    public void uploadProfileImage(File imageFile) {
        String userId = LocalStorageService.sharedInstance().getLocalFileStore().
                getString(getApplication().getApplicationContext(), SharedPreferenceKeys.USER_ID);
        NetworkService.sharedInstance().getNetworkClient().uploadImageWithMultipartFormData(getApplication(), UrlConstants.UPDATE_PROFILE_IMAGE + userId, true, Collections.emptyMap(), imageFile, "profile_image", NetworkConstants.PUT_REQUEST, (type, response, errorMessage) -> {});
    }

    public void getSummary(final UILevelNetworkCallback summaryCallback) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.SUMMARY_URL,
                true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            List<Summary> summaryList = new ArrayList<>();
                            SummaryParser summaryParser = new SummaryParser();
                            Summary summary = summaryParser.summaryResponseByParsingJsonResponse(response);
                            if (summary == null) summary = new Summary();
                            summaryList.add(summary);
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
                });
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }
}
