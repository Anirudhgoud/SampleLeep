package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.MapAttribute;
import com.goleep.driverapp.helpers.uimodels.ReportAttrribute;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.NewCustomerActivity;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.MapAddressParser;
import com.goleep.driverapp.services.network.jsonparsers.ReportsDataParser;
import com.goleep.driverapp.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 03/04/2018.
 */

public class NewCustomerViewModel extends AndroidViewModel {
    String TAG= "NewCusViewModLog";
    public NewCustomerViewModel(@NonNull Application application) {
        super(application);
    }
    public void getAddressFromLatitudeLongitude(final UILevelNetworkCallback newCustomerCallBack,String latitude, String longitude){
        LogUtils.debug(TAG, UrlConstants.LAT_LONG_TO_ADDRESS_URL+getQueryParameter(latitude,longitude));
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.LAT_LONG_TO_ADDRESS_URL +getQueryParameter(latitude,longitude),
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
    private  String getQueryParameter(String latitude,String longitude){
        return "latlng="+latitude+","+longitude+"&key="+getApplication().getResources().getString(R.string.map_key);
    }

}
