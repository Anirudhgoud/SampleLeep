package com.tracotech.tracoman.viewmodels.information;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.helpers.uimodels.ReturnOrderItem;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.services.network.jsonparsers.OrderItemParser;
import com.tracotech.tracoman.services.room.AppDatabase;
import com.tracotech.tracoman.services.room.RoomDBService;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.services.room.entities.ReturnOrderEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 21/03/18.
 */

public class HistoryDetailsViewModel extends AndroidViewModel {
    private AppDatabase leepDatabase;
    private int orderType;
    private DeliveryOrderEntity deliveryOrderEntity;
    private ReturnOrderEntity returnOrderEntity;
    private List<ReturnOrderItem> roItems = new ArrayList<>();
    private List<OrderItemEntity> doItems = new ArrayList<>();

    public HistoryDetailsViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public DeliveryOrderEntity getDeliveryOrderEntity(int doId){
        deliveryOrderEntity = leepDatabase.deliveryOrderDao().deliveryOrder(doId);
        return deliveryOrderEntity;
    }

    public ReturnOrderEntity getReturnOrderEntity(long roNUmber){
        returnOrderEntity = leepDatabase.returnOrderDao().getReturnOrderEntity(roNUmber);
        return returnOrderEntity;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public DeliveryOrderEntity getDeliveryOrderEntity() {
        return deliveryOrderEntity;
    }

    public void setDeliveryOrderEntity(DeliveryOrderEntity deliveryOrderEntity) {
        this.deliveryOrderEntity = deliveryOrderEntity;
    }

    public ReturnOrderEntity getReturnOrderEntity() {
        return returnOrderEntity;
    }

    public void setReturnOrderEntity(ReturnOrderEntity returnOrderEntity) {
        this.returnOrderEntity = returnOrderEntity;
    }

    public void fetchDoItems(final int doId, UILevelNetworkCallback orderItemsCallBack){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true,
                (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            OrderItemParser orderItemParser = new OrderItemParser();
                            doItems = orderItemParser.
                                    orderItemsByParsingJsonResponse(response, doId);
                            orderItemsCallBack.onResponseReceived(doItems, false,
                                    null, false);
                            break;

                        case NetworkConstants.NETWORK_ERROR:
                        case NetworkConstants.FAILURE:
                            orderItemsCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            orderItemsCallBack.onResponseReceived(null, false,
                                    null, true);
                            break;
                    }
                });
    }

    public void fetchRoItems(long orderId, UILevelNetworkCallback orderItemsCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(),
                UrlConstants.DRIVER_RETURNED_ORDERS + "/" + orderId, true, (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            OrderItemParser orderItemParser = new OrderItemParser();
                            roItems = orderItemParser.
                                    returnOrderItemsByParsingJsonResponse(response, orderId);
                            orderItemsCallBack.onResponseReceived(roItems, false,
                                    null, false);
                            break;

                        case NetworkConstants.NETWORK_ERROR:
                        case NetworkConstants.FAILURE:
                            orderItemsCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            orderItemsCallBack.onResponseReceived(null, false,
                                    null, true);
                            break;
                    }
                });
    }

    public List<OrderItemEntity> getDoItems() {
        return doItems;
    }

    public List<ReturnOrderItem> getRoItems() {
        return roItems;
    }
}
