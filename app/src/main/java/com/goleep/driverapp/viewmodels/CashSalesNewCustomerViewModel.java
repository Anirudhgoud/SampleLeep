package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Business;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.BusinessCategoryParser;
import com.goleep.driverapp.services.network.jsonparsers.GetBusinessesDataParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by shubham on 03/04/2018.
 */

public class CashSalesNewCustomerViewModel extends AndroidViewModel {

    private List<Business> listBusinessData;
    private String strBusinesstype;
    private int businessTypeId, selectedBusinessId;
    public Bundle bundle;
    private List<Business> listGetBusinessesData;

    public List<Business> getListBusinessData() {
        return listBusinessData;
    }

    public String getStrBusinesstype() {
        return strBusinesstype;
    }

    public int getBusinessTypeId() {
        return businessTypeId;
    }

    public int getSelectedBusinessId() {
        return selectedBusinessId;
    }

    public List<Business> getListGetBusinessesData() {
        return listGetBusinessesData;
    }


    public void setListBusinessData(List<Business> listBusinessData) {
        this.listBusinessData = listBusinessData;
    }

    public void setStrBusinesstype(String strBusinesstype) {
        this.strBusinesstype = strBusinesstype;
    }

    public void setBusinessTypeId(int businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public void setSelectedBusinessId(int selectedBusinessId) {
        this.selectedBusinessId = selectedBusinessId;
    }

    public void setListGetBusinessesData(List<Business> listGetBusinessesData) {
        this.listGetBusinessesData = listGetBusinessesData;
    }

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
                                List<Business> listBusinessData = new BusinessCategoryParser().reportsDataByParsingJsonResponse(userObj);
                                newBusinessCategoryCallBack.onResponseReceived(listBusinessData, false, null, false);
                                break;
                            case NetworkConstants.FAILURE:
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
                                List<Business> lisGetBusinessesData = new GetBusinessesDataParser().reportsDataByParsingJsonResponse(userObj);
                                newBusinessCategoryCallBack.onResponseReceived(lisGetBusinessesData, false, null, false);
                                break;
                            case NetworkConstants.FAILURE:
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
