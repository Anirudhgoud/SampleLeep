package com.tracotech.tracoman.viewmodels.dropoff.cashsales;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.SharedPreferenceKeys;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.services.network.jsonparsers.DeliveryOrderParser;
import com.tracotech.tracoman.services.network.jsonparsers.OrderItemParser;
import com.tracotech.tracoman.services.room.AppDatabase;
import com.tracotech.tracoman.services.room.RoomDBService;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.services.room.entities.WarehouseEntity;
import com.tracotech.tracoman.services.storage.LocalStorageService;

import java.util.List;

/**
 * Created by vishalm on 22/02/18.
 */

public class CashSalesViewModel extends AndroidViewModel {
    private LiveData<DeliveryOrderEntity> driverDO;
    private LiveData<List<OrderItemEntity>> driverDoDetails;
    private  AppDatabase leepDatabase;
    private WarehouseEntity warehouseEntity;

    public CashSalesViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public LiveData<DeliveryOrderEntity> getDriverDo(){
        driverDO = leepDatabase.deliveryOrderDao().getDriverDo(warehouseEntity.getId());
        return driverDO;
    }

    public LiveData<List<OrderItemEntity>> getDriverDoDetails(Integer id) {
        driverDoDetails = leepDatabase.deliveryOrderItemDao().getDeliveryOrderItems(id);
        return driverDoDetails;
    }

    public void setWarehouse(int warehouseId) {
        warehouseEntity = leepDatabase.warehouseDao().getWarehouse(warehouseId);
    }

    public WarehouseEntity getWarehouse() {
        return warehouseEntity;
    }

    public void fetchDriverDo(final UILevelNetworkCallback driverDOCallback){
        int driverId = LocalStorageService.sharedInstance().getLocalFileStore().getInt(
                getApplication().getApplicationContext(), SharedPreferenceKeys.DRIVER_ID);
        String url = UrlConstants.DELIVERY_ORDERS_URL + "?type=driver&assignees="+driverId+"&statuses=assigned";
        if(warehouseEntity != null)
            url += "&source_locations="+warehouseEntity.getId();
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), url,
                true, (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            DeliveryOrderParser deliveryOrderParser = new DeliveryOrderParser();
                            List<DeliveryOrderEntity> deliveryOrdersList = deliveryOrderParser.
                                    deliveryOrdersByParsingJsonResponse(response);
                            if(deliveryOrdersList.size() > 0){
                                DeliveryOrderEntity driverDO = deliveryOrdersList.get(0);
                                leepDatabase.deliveryOrderDao().insertDeliveryOrder(driverDO);
                            }
                            break;

                            case NetworkConstants.UNAUTHORIZED :
                                driverDOCallback.onResponseReceived(null,
                                        false, errorMessage, true);
                                break;
                    }
                });
    }

    public void fetchDriverDoDetails(final int doId, final UILevelNetworkCallback doDetailsCallback){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(),
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true, (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            OrderItemParser orderItemParser = new OrderItemParser();
                            leepDatabase.deliveryOrderItemDao().deleteAndInsertItems(doId,
                                    orderItemParser.orderItemsByParsingJsonResponse(response, doId));
                            break;

                        case NetworkConstants.UNAUTHORIZED :
                            doDetailsCallback.onResponseReceived(null,
                                    false, errorMessage, true);
                            break;
                    }
                });
    }
}
