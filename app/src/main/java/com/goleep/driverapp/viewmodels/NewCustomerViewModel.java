package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Business;
import com.goleep.driverapp.helpers.uimodels.Country;
import com.goleep.driverapp.helpers.uimodels.CustomerInfo;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.helpers.uimodels.MapData;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.BusinessDataParser;
import com.goleep.driverapp.services.network.jsonparsers.LocationParser;
import com.goleep.driverapp.services.network.jsonparsers.MapAddressParser;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubham on 03/04/2018.
 */

public class NewCustomerViewModel extends AndroidViewModel {
    private List<Country> countryAttributeList;
    private int countryId;
    private String postalCode;
    private LatLng lastLatLng;
    private CustomerInfo customerInfo;


    public void setCountryAttributeList(List<Country> countryAttributeList) {
        this.countryAttributeList = countryAttributeList;
    }

    public LatLng getLastLatLng() {
        return lastLatLng;
    }

    public void setLastLatLng(LatLng lastLatLng) {
        this.lastLatLng = lastLatLng;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public List<Country> getCountryAttributeList() {
        return countryAttributeList;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public NewCustomerViewModel(@NonNull Application application) {
        super(application);
    }

    public void getAddressFromLatitudeLongitude(final UILevelNetworkCallback newCustomerCallBack, String latitude, String longitude) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.LAT_LONG_TO_ADDRESS_URL + getQueryParameter(latitude, longitude),
                null, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type) {
                            case NetworkConstants.SUCCESS:
                                JSONObject userObj = (JSONObject) response.opt(0);
                                MapData mapData = new MapAddressParser().reportsDataByParsingJsonResponse(userObj);
                                List<MapData> lisMapData = new ArrayList<>();
                                lisMapData.add(mapData);
                                newCustomerCallBack.onResponseReceived(lisMapData, false, null, false);
                                break;
                            case NetworkConstants.FAILURE:
                                newCustomerCallBack.onResponseReceived(null, false, errorMessage, false);
                                break;
                            case NetworkConstants.NETWORK_ERROR:
                                newCustomerCallBack.onResponseReceived(null, true, errorMessage, false);
                                break;
                            case NetworkConstants.UNAUTHORIZED:
                                newCustomerCallBack.onResponseReceived(null, false, errorMessage, true);
                        }

                    }
                });
    }

    private String getQueryParameter(String latitude, String longitude) {
        return "latlng=" + latitude + "," + longitude + "&key=" + getApplication().getResources().getString(R.string.map_key);
    }

    public void createNewCustomer(final UILevelNetworkCallback newCustomerCallBack, int countryId, String name, String contactEmail, String contactName, String contactNumber, String designation, String postalCode, int businessCategoryId) {
        Map<String, Object> body = new HashMap<>();
        body.put("country_id", countryId);
        body.put("name", name);
        body.put("contact_email_1", contactEmail);
        body.put("contact_name_1", contactName);
        body.put("contact_number_1", contactNumber);
        body.put("designation_1", designation);
        body.put("postal_code", postalCode);
        body.put("business_category_id", businessCategoryId);
        NetworkService.sharedInstance().getNetworkClient().makeFormPostRequest(getApplication(), UrlConstants.BUSINESSES_URL, true, body, new NetworkAPICallback() {
            @Override
            public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                switch (type) {
                    case NetworkConstants.SUCCESS:
                        JSONObject userObj = (JSONObject) response.opt(0);
                        Business business = new BusinessDataParser().customerBusinessDataByparsingJsonResponse(userObj);
                        List<Business> listBusinessAttribute = new ArrayList<>();
                        listBusinessAttribute.add(business);
                        newCustomerCallBack.onResponseReceived(listBusinessAttribute, false, null, false);
                        break;
                    case NetworkConstants.FAILURE:
                        newCustomerCallBack.onResponseReceived(null, false, errorMessage, false);
                        break;
                    case NetworkConstants.NETWORK_ERROR:
                        newCustomerCallBack.onResponseReceived(null, true, errorMessage, false);
                        break;
                    case NetworkConstants.UNAUTHORIZED:
                        newCustomerCallBack.onResponseReceived(null, false, errorMessage, true);
                }
            }
        });
    }

    public void createNewLocation(final UILevelNetworkCallback newCustomerCallBack, String name, String addressLine1, String addressLine2, String city,
                                  String state, int countryId, String pincode, String area, double latitude, double longitude,
                                  String contactName1, String designation, String email, String phone, int businessId) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("address_line_1", addressLine1);
        body.put("address_line_2", addressLine2);
        body.put("city", city);
        body.put("state", state);
        body.put("country_id", countryId);
        body.put("pin_code", pincode);
        body.put("area", area);
        body.put("latitude", latitude);
        body.put("longitude", longitude);
        body.put("contact_name_1", contactName1);
        body.put("contact_designation_1", designation);
        body.put("contact_email_1", email);
        body.put("contact_phone_1", phone);
        body.put("redistribution_centre", name);
        NetworkService.sharedInstance().getNetworkClient().makeJsonPostRequest(getApplication(),
                UrlConstants.BUSINESSES_URL + "/" + businessId + "/locations", true, body, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type) {
                            case NetworkConstants.SUCCESS:
                                JSONObject userObj = (JSONObject) response.opt(0);
                                Location location = new LocationParser().getBusinessLocation(userObj);
                                List<Location> listLocation = new ArrayList<>();
                                listLocation.add(location);
                                newCustomerCallBack.onResponseReceived(listLocation, false, null, false);
                                break;
                            case NetworkConstants.FAILURE:
                                newCustomerCallBack.onResponseReceived(null, false, errorMessage, false);
                                break;
                            case NetworkConstants.NETWORK_ERROR:
                                newCustomerCallBack.onResponseReceived(null, true, errorMessage, false);
                                break;
                            case NetworkConstants.UNAUTHORIZED:
                                newCustomerCallBack.onResponseReceived(null, false, errorMessage, true);
                        }
                    }
                });
    }
}
