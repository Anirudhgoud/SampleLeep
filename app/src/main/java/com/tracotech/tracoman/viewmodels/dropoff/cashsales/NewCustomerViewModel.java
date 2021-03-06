package com.tracotech.tracoman.viewmodels.dropoff.cashsales;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.helpers.uimodels.Address;
import com.tracotech.tracoman.helpers.uimodels.Business;
import com.tracotech.tracoman.helpers.uimodels.Country;
import com.tracotech.tracoman.helpers.uimodels.CustomerInfo;
import com.tracotech.tracoman.helpers.uimodels.Location;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.services.network.jsonparsers.BusinessDataParser;
import com.tracotech.tracoman.services.network.jsonparsers.LocationParser;
import com.google.android.gms.maps.model.LatLng;

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

    public void getAddressFromLatitudeLongitude(final UILevelNetworkCallback newCustomerCallBack, double latitude, double longitude) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.LAT_LONG_TO_ADDRESS_URL + getQueryParameter(latitude, longitude),
                null, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            Address address = new LocationParser().getAddressByParsingJsonResponse(response);
                            List<Address> lisMapData = new ArrayList<>();
                            lisMapData.add(address);
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

                });
    }

    private String getQueryParameter(double latitude, double longitude) {
        return "latlng=" + latitude + "," + longitude + "&key=" + getApplication().getResources().getString(R.string.google_maps_key);
    }

    public void createNewCustomer(final UILevelNetworkCallback newCustomerCallBack, int countryId,
                                  String name, String contactEmail, String contactName, String contactNumber,
                                  String designation, String postalCode, int businessCategoryId, String dialCode,
                                  String addressLine1, String addressLine2, String city, String state) {
        Map<String, Object> body = getNewCustomerBody(countryId, name, contactEmail, contactName,
                contactNumber, designation, postalCode, businessCategoryId, dialCode, addressLine1, addressLine2, city, state);
        NetworkService.sharedInstance().getNetworkClient().makeFormPostRequest(getApplication(), UrlConstants.BUSINESSES_URL, true, body, (type, response, errorMessage) -> {
            switch (type) {
                case NetworkConstants.SUCCESS:
                    Business business = new BusinessDataParser().customerBusinessDataByParsingJsonResponse(response);
                    List<Business> listBusinessAttribute = new ArrayList<>();
                    listBusinessAttribute.add(business);
                    newCustomerCallBack.onResponseReceived(listBusinessAttribute, false, null, false);
                    break;
                case NetworkConstants.FAILURE:
                    newCustomerCallBack.onResponseReceived(null, true, errorMessage, false);
                    break;
                case NetworkConstants.NETWORK_ERROR:
                    newCustomerCallBack.onResponseReceived(null, true, errorMessage, false);
                    break;
                case NetworkConstants.UNAUTHORIZED:
                    newCustomerCallBack.onResponseReceived(null, false, errorMessage, true);
            }
        });
    }
    private  Map<String, Object> getNewCustomerBody( int countryId, String name, String contactEmail,
                                                     String contactName, String contactNumber,
                                                     String designation, String postalCode,
                                                     int businessCategoryId, String dialcode,
                                                     String addressLine1, String addressLine2, String city, String state){
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("business_category_id", businessCategoryId);
        body.put("address_line_1", addressLine1);
        body.put("address_line_2", addressLine2);
        body.put("city", city);
        body.put("state", state);
        body.put("postal_code", postalCode);
        body.put("contact_name_1", contactName);
        body.put("designation_1", designation);
        body.put("contact_email_1", contactEmail);
        body.put("contact_number_1", contactNumber);
        body.put("country_id", countryId);
        body.put("country_code", dialcode);
        return  body;
    }

    public void createNewLocation(final UILevelNetworkCallback newCustomerCallBack, String name, String addressLine1, String addressLine2, String city,
                                  String state, int countryId, String pincode, String area, double latitude, double longitude,
                                  String contactName1, String designation, String email, String phone, int businessId) {
        Map<String, Object> body = getNewLocationBody(name, addressLine1, addressLine2,city, state, countryId, pincode, area, latitude, longitude, contactName1, designation, email, phone);
        NetworkService.sharedInstance().getNetworkClient().makeJsonPostRequest(getApplication(),
                UrlConstants.BUSINESSES_URL + "/" + businessId + "/locations", true, body, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            Location location = new LocationParser().getBusinessLocation(response);
                            List<Location> listLocation = new ArrayList<>();
                            listLocation.add(location);
                            newCustomerCallBack.onResponseReceived(listLocation, false, null, false);
                            break;
                        case NetworkConstants.FAILURE:
                            newCustomerCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;
                        case NetworkConstants.NETWORK_ERROR:
                            newCustomerCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;
                        case NetworkConstants.UNAUTHORIZED:
                            newCustomerCallBack.onResponseReceived(null, false, errorMessage, true);
                    }
                });
    }
    private  Map<String, Object> getNewLocationBody(String name, String addressLine1, String addressLine2, String city,
                                                       String state, int countryId, String pincode, String area, double latitude, double longitude,
                                                       String contactName1, String designation, String email, String phone){
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
        body.put("redistribution_centre", false);
        return body ;
    }
}
