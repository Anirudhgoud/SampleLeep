package com.goleep.driverapp.viewmodels.information;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.ReturnOrderItem;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.OrderItemParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;

import java.util.List;

/**
 * Created by vishalm on 21/03/18.
 */

public class HistoryDetailsViewModel extends AndroidViewModel {
    private AppDatabase leepDatabase;
    public HistoryDetailsViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public DeliveryOrderEntity getDeliveryOrderEntity(int doId){
        return leepDatabase.deliveryOrderDao().deliveryOrder(doId);
    }

    public ReturnOrderEntity getReturnOrderEntity(long roNUmber){
        return leepDatabase.returnOrderDao().getReturnOrderEntity(roNUmber);
    }

    public void fetchDoItems(final int doId, UILevelNetworkCallback doDetailsCallback){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true,
                (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            OrderItemParser orderItemParser = new OrderItemParser();
                            List<OrderItemEntity> orderItems = orderItemParser.
                                    orderItemsByParsingJsonResponse(response, doId);
                            doDetailsCallback.onResponseReceived(orderItems, false,
                                    null, false);
                            break;
                        case NetworkConstants.UNAUTHORIZED:
                            doDetailsCallback.onResponseReceived(null, false,
                                    null, true);
                            break;
                    }
                });
    }

    public void fetchRoItems(long orderId, UILevelNetworkCallback orderItemsCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(),
                UrlConstants.RETURNED_ORDERS + "/" + orderId, true, (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            OrderItemParser orderItemParser = new OrderItemParser();
                            List<ReturnOrderItem> orderItems = orderItemParser.
                                    returnOrderItemsByParsingJsonResponse(response, orderId);
                            orderItemsCallBack.onResponseReceived(orderItems, false,
                                    null, false);
                            break;
                        case NetworkConstants.UNAUTHORIZED:
                            orderItemsCallBack.onResponseReceived(null, false,
                                    null, true);
                            break;
                    }
                });
    }
}
