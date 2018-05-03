package com.tracotech.tracoman.viewmodels.dropoff.cashsales;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.services.network.jsonparsers.CustomerParser;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by anurag on 28/03/18.
 */

public class CashSalesExistingCustomerViewModel extends AndroidViewModel {


    public CashSalesExistingCustomerViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchCustomerList(boolean lastDeliveryDateRequired, LatLng currentLocation, String searchText, final UILevelNetworkCallback customerListNetworkCallBack) {
        String url = UrlConstants.CONSUMER_LOCATIONS_URL + "?last_delivery_date=" + lastDeliveryDateRequired;
        if(currentLocation != null) url += "&latitude=" + currentLocation.latitude + "&longitude=" + currentLocation.longitude;
        if(searchText != null) url += "&text=" + searchText;

        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                url, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            CustomerParser customerParser = new CustomerParser();
                            List<Customer> customerList = customerParser.
                                    customerListByParsingJsonResponse(response);
                            customerListNetworkCallBack.onResponseReceived(customerList, false,
                                    null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            customerListNetworkCallBack.onResponseReceived(null, true,
                                    errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            customerListNetworkCallBack.onResponseReceived(null,
                                    false, errorMessage, true);
                            break;

                    }
                });
    }


}
