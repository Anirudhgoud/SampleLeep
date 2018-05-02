package com.goleep.driverapp.viewmodels.dropoff.deliveryorders;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
import com.goleep.driverapp.services.room.entities.WarehouseEntity;
import com.goleep.driverapp.services.storage.LocalStorageService;

import java.util.List;

/**
 * Created by vishalm on 27/02/18.
 */


public class DeliveryOrderViewModel extends AndroidViewModel {

    protected AppDatabase leepDatabase;
    private LiveData<List<DeliveryOrderEntity>> deliveryOrders = new MutableLiveData<>();

    public List<DeliveryOrderEntity> getDeliveryOrders() {
        return deliveryOrders.getValue();
    }
    private WarehouseEntity warehouseEntity;

    public static final String TYPE_CUSTOMER = "customer";
    public static final String STATUS_IN_TRANSIT = "in_transit";
    public static final String STATUS_ASSIGNED = "assigned";
    public static final String STATUS_DELIVERED = "delivered";

    public DeliveryOrderViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }
    public LiveData<List<DeliveryOrderEntity>> getDeliveryOrders(String type, String status) {
        deliveryOrders = leepDatabase.deliveryOrderDao().getCustomerDeliveryOrders(type, status);
        return deliveryOrders;
    }

    public LiveData<List<DeliveryOrderEntity>> getDeliveryOrders(String type, String status, int sourceLocationId) {
        deliveryOrders = leepDatabase.deliveryOrderDao().getCustomerDeliveryOrders(type, status, sourceLocationId);
        return deliveryOrders;
    }

    public void setWarehouse(int warehouseId) {
        warehouseEntity = leepDatabase.warehouseDao().getWarehouse(warehouseId);
    }

    public WarehouseEntity getWarehouse() {
        return warehouseEntity;
    }

    public void fetchAllDeliveryOrders(final UILevelNetworkCallback doNetworkCallBack, String status,
                                       String actualDeliveryStartDate, String actualDeliveryEndDate, int wareHouseId, String dotype) {
        int driverId = LocalStorageService.sharedInstance().getLocalFileStore().getInt(
                getApplication().getApplicationContext(),SharedPreferenceKeys.DRIVER_ID);
        String url = UrlConstants.DELIVERY_ORDERS_URL + "?assignees=" + driverId;
        if (status != null) url += "&statuses=" + status;
        if (actualDeliveryStartDate != null && actualDeliveryStartDate != null) url += "&act_del_start_date=" + actualDeliveryStartDate + "&act_del_end_date=" + actualDeliveryStartDate;
        if (wareHouseId != -1) url += "&source_locations="+wareHouseId;
        if (dotype != null) url += "&type="+dotype;
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                url, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            DeliveryOrderParser deliveryOrderParser = new DeliveryOrderParser();
                            List<DeliveryOrderEntity> deliveryOrdersList = deliveryOrderParser.
                                    deliveryOrdersByParsingJsonResponse(response);
                            leepDatabase.deliveryOrderDao().updateAllDeliveryOrders(deliveryOrdersList);
                            doNetworkCallBack.onResponseReceived(deliveryOrdersList, false,
                                    null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            doNetworkCallBack.onResponseReceived(null, true,
                                    errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            doNetworkCallBack.onResponseReceived(null,
                                    false, errorMessage, true);
                            break;

                    }
                });
    }
}
