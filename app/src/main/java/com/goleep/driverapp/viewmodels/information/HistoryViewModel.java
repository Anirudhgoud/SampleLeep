package com.goleep.driverapp.viewmodels.information;

import android.app.Application;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.ReturnOrderParser;
import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.viewmodels.dropoff.deliveryorders.DeliveryOrderViewModel;

import java.util.List;

/**
 * Created by vishalm on 21/03/18.
 */

public class HistoryViewModel extends DeliveryOrderViewModel {

    private String startDate = "";
    private String endDate = "";

    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchDeliveryOrders(String startDate, String endDate, UILevelNetworkCallback doNetworkCallback){
        if(startDate != null && endDate != null &&
                !startDate.isEmpty() && !endDate.isEmpty()){
            this.startDate = startDate;
            this.endDate = endDate;
            fetchAllDeliveryOrders(doNetworkCallback, "delivered", startDate, endDate, -1);
        }
    }


    public void fetchDeliveryOrders(UILevelNetworkCallback doNetworkCallback){
        if(startDate != null && endDate != null &&
                !startDate.isEmpty() && !endDate.isEmpty()){
            fetchAllDeliveryOrders(doNetworkCallback, "delivered", startDate, endDate, -1);
        }
    }

    public void fetchReturnedOrders(String startDate, String endDate, UILevelNetworkCallback doNetworkCallback){
        String url = UrlConstants.DRIVER_RETURNED_ORDERS;
        if(startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            url +="?start_date="+startDate+"&end_date="+endDate;
            this.startDate = startDate;
            this.endDate = endDate;
        }
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), url,
                true, (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            ReturnOrderParser parser = new ReturnOrderParser();
                            List<ReturnOrderEntity> entities = parser.parserReturnOrderResponse(response);
                            leepDatabase.returnOrderDao().updateAllDeliveryOrders(entities);
                            doNetworkCallback.onResponseReceived(entities, false, null, false);
                            break;
                        case NetworkConstants.UNAUTHORIZED:
                            doNetworkCallback.onResponseReceived(null, false, null, true);
                            break;
                        case NetworkConstants.NETWORK_ERROR:
                        case NetworkConstants.FAILURE:
                            doNetworkCallback.onResponseReceived(null, true, errorMessage, true);
                            break;
                    }
                });
    }


    public void fetchReturnedOrders(UILevelNetworkCallback doNetworkCallback){
        if(startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            fetchReturnedOrders(startDate, endDate, doNetworkCallback);
        }
    }
}
