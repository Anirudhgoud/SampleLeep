package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.BusinessAttribute;
import com.goleep.driverapp.helpers.uimodels.MapAttribute;
import com.goleep.driverapp.helpers.uimodels.ReportAttrribute;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.NewCustomerActivity;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.BusinessDataParser;
import com.goleep.driverapp.services.network.jsonparsers.MapAddressParser;
import com.goleep.driverapp.services.network.jsonparsers.ReportsDataParser;
import com.goleep.driverapp.utils.LogUtils;

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
    String TAG = "NewCusViewModLog";
    Context context;

    public NewCustomerViewModel(@NonNull Application application) {
        super(application);
        context=getApplication();
    }

    public void getAddressFromLatitudeLongitude(final UILevelNetworkCallback newCustomerCallBack, String latitude, String longitude) {
        LogUtils.debug(TAG, UrlConstants.LAT_LONG_TO_ADDRESS_URL + getQueryParameter(latitude, longitude));
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.LAT_LONG_TO_ADDRESS_URL + getQueryParameter(latitude, longitude),
                null, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type) {
                            case NetworkConstants.SUCCESS:
                                JSONObject userObj = (JSONObject) response.opt(0);
                                MapAttribute mapAttribute = new MapAddressParser().reportsDataByParsingJsonResponse(userObj);
                                List<MapAttribute> lisMapAttributes = new ArrayList<>();
                                lisMapAttributes.add(mapAttribute);
                                newCustomerCallBack.onResponseReceived(lisMapAttributes, false, null, false);
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
        Map<String ,Object> body = new HashMap<>();
        body.put("country_id",countryId);
        body.put("name",name);
        body.put("contact_email_1",contactEmail);
        body.put("contact_name_1",contactName);
        body.put("contact_number_1",contactNumber);
        body.put("designation_1",designation);
        body.put("postal_code",postalCode);
        body.put("business_category_id",businessCategoryId);
        NetworkService.sharedInstance().getNetworkClient().makeFormPostRequest(getApplication(), UrlConstants.BUSINESSES_URL, true, body, new NetworkAPICallback() {
            @Override
            public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                switch (type) {
                    case NetworkConstants.SUCCESS:
                        JSONObject userObj = (JSONObject) response.opt(0);
                        BusinessAttribute businessAttribute = new BusinessDataParser().reportsDataByParsingJsonResponse(userObj);
                        List<BusinessAttribute> listBusinessAttribute = new ArrayList<>();
                        listBusinessAttribute.add(businessAttribute);
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

}
