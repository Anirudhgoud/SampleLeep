package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.DeliveryOrderParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.storage.LocalStorageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 27/02/18.
 */

public class DeliveryOrderViewModel extends AndroidViewModel {
    protected AppDatabase leepDatabase;
    private LiveData<List<DeliveryOrderEntity>> deliveryOrders;

    public static final String TYPE_CUSTOMER = "customer";
    public static final String TYPE_DRIVER = "driver";
    public static final String STATUS_IN_TRANSIT = "in_transit";
    public static final String STATUS_ASSIGNED = "assigned";

    public DeliveryOrderViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }
    public LiveData<List<DeliveryOrderEntity>> getDeliveryOrders(String type, String status) {
        deliveryOrders = leepDatabase.deliveryOrderDao().getCustomerDeliveryOrders(type, status);
        return deliveryOrders;
    }

    public void fetchAllDeliveryOrders(final UILevelNetworkCallback doNetworkCallBack){
        int driverId = LocalStorageService.sharedInstance().getLocalFileStore().getInt(
                getApplication().getApplicationContext(),SharedPreferenceKeys.DRIVER_ID);
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                UrlConstants.DELIVERY_ORDERS_URL+"?assignees="+driverId,
                true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            DeliveryOrderParser deliveryOrderParser = new DeliveryOrderParser();
                            List<DeliveryOrderEntity> deliveryOrdersList = deliveryOrderParser.
                                    deliveryOrdersByParsingJsonResponse(response);
                            leepDatabase.deliveryOrderDao().updateAllDeliveryOrders(deliveryOrdersList);
                            doNetworkCallBack.onResponseReceived(new ArrayList<>(), false, null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            doNetworkCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            doNetworkCallBack.onResponseReceived(null,
                                    false, errorMessage, true);
                            break;

                    }
                });
    }
}
