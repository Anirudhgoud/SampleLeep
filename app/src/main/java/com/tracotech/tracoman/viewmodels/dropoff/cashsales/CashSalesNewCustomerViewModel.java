package com.tracotech.tracoman.viewmodels.dropoff.cashsales;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.helpers.uimodels.Business;
import com.tracotech.tracoman.helpers.uimodels.Country;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.services.network.jsonparsers.BusinessDataParser;

import java.util.List;

/**
 * Created by shubham on 03/04/2018.
 */

public class CashSalesNewCustomerViewModel extends AndroidViewModel {

    private String strBusinesstype;
    private int businessTypeId, selectedBusinessId;
    private List<Business> listGetBusinessesData;
    private List<Country> countryList;
    private Country selectedCountry;

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

    public List<Country> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public CashSalesNewCustomerViewModel(@NonNull Application application) {
        super(application);
    }

    public void getBusinessTypes(final UILevelNetworkCallback newBusinessCategoryCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.BUSINESS_CATEGORIES_URL,
                null, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            List<Business> listBusinessData = new BusinessDataParser().businessCategoryDataByParsingJsonResponse(response);
                            newBusinessCategoryCallBack.onResponseReceived(listBusinessData, false, null, false);
                            break;
                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            newBusinessCategoryCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;
                        case NetworkConstants.UNAUTHORIZED:
                            newBusinessCategoryCallBack.onResponseReceived(null, false, errorMessage, true);
                    }
                });
    }

    public void getBusinessData(final UILevelNetworkCallback newBusinessCategoryCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.BUSINESSES_URL,
                null, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            List<Business> lisGetBusinessesData = new BusinessDataParser().businessCategoryDataByParsingJsonResponse(response);
                            newBusinessCategoryCallBack.onResponseReceived(lisGetBusinessesData, false, null, false);
                            break;
                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            newBusinessCategoryCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;
                        case NetworkConstants.UNAUTHORIZED:
                            newBusinessCategoryCallBack.onResponseReceived(null, false, errorMessage, true);
                    }
                });
    }
}
