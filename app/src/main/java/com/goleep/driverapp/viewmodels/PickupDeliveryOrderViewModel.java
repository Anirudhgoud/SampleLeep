package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.OrderItemParser;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;

import org.json.JSONArray;

import java.util.List;



/**
 * Created by vishalm on 27/02/18.
 */

public class PickupDeliveryOrderViewModel extends DropOffDeliveryOrdersViewModel {
    private Context context;
    private LiveData<List<OrderItemEntity>> doDetailsLiveData;

    public PickupDeliveryOrderViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

    public void fetchDoItems(final int doId){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context,
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true,
                new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                OrderItemParser orderItemParser = new OrderItemParser();
                                leepDatabase.deliveryOrderItemDao().deleteAndInsertItems(doId,
                                        orderItemParser.orderItemsByParsingJsonResponse(response, doId));
                                break;
                        }
                    }
                });
    }



    public LiveData<List<OrderItemEntity>> getDoDetails(Integer id) {
        doDetailsLiveData = leepDatabase.deliveryOrderItemDao().getDeliveryOrderItems(id);
        return doDetailsLiveData;
    }

    public String getWareHouseNameAddress(){
        DriverEntity driverEntity = RoomDBService.sharedInstance().getDatabase(context).driverDao().getDriver();
        return driverEntity.getAddressLine1()+", "+ driverEntity.getAddressLine2();
    }
}