package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.BusinessCategoryAttribute;
import com.goleep.driverapp.helpers.uimodels.GetBusinessesData;
import com.goleep.driverapp.helpers.uimodels.MapAttribute;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.BusinessCategoryParser;
import com.goleep.driverapp.services.network.jsonparsers.GetBusinessesDataParser;
import com.goleep.driverapp.services.network.jsonparsers.MapAddressParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 03/04/2018.
 */

public class CashSalesNewCustomerViewModel extends AndroidViewModel {
    public CashSalesNewCustomerViewModel(@NonNull Application application) {
        super(application);
    }

    public void getBusinessTypes(final UILevelNetworkCallback newBusinessCategoryCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.BUSINESS_CATEGORIES_URL,
                null, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type) {
                            case NetworkConstants.SUCCESS:
                                JSONObject userObj = (JSONObject) response.opt(0);
                                List<BusinessCategoryAttribute> listBusinessCategoryAttribute = new BusinessCategoryParser().reportsDataByParsingJsonResponse(userObj);
                                newBusinessCategoryCallBack.onResponseReceived(listBusinessCategoryAttribute, false, null, false);
                                break;
                            case NetworkConstants.FAILURE:
                                newBusinessCategoryCallBack.onResponseReceived(null, false, errorMessage, false);
                                break;
                            case NetworkConstants.NETWORK_ERROR:
                                newBusinessCategoryCallBack.onResponseReceived(null, true, errorMessage, false);
                                break;
                            case NetworkConstants.UNAUTHORIZED:
                                newBusinessCategoryCallBack.onResponseReceived(null, false, errorMessage, true);
                        }

                    }
                });
    }

    public void getBusinessesData(final UILevelNetworkCallback newBusinessCategoryCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.BUSINESSES_URL,
                null, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type) {
                            case NetworkConstants.SUCCESS:
                                JSONObject userObj = (JSONObject) response.opt(0);
                                List<GetBusinessesData> lisGetBusinessesData = new GetBusinessesDataParser().reportsDataByParsingJsonResponse(userObj);
                                newBusinessCategoryCallBack.onResponseReceived(lisGetBusinessesData, false, null, false);
                                break;
                            case NetworkConstants.FAILURE:
                                newBusinessCategoryCallBack.onResponseReceived(null, false, errorMessage, false);
                                break;
                            case NetworkConstants.NETWORK_ERROR:
                                newBusinessCategoryCallBack.onResponseReceived(null, true, errorMessage, false);
                                break;
                            case NetworkConstants.UNAUTHORIZED:
                                newBusinessCategoryCallBack.onResponseReceived(null, false, errorMessage, true);
                        }

                    }
                });
    }
}
