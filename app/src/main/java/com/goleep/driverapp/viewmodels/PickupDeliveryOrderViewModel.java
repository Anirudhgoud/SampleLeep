package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.OrderItemParser;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    public List<OrderItemEntity> getOrderItemsList(int doId){
        return leepDatabase.deliveryOrderItemDao().getDOrderItemssList(doId);
    }

    public LiveData<List<OrderItemEntity>> getDoDetails(Integer id) {
        doDetailsLiveData = leepDatabase.deliveryOrderItemDao().getDeliveryOrderItems(id);
        return doDetailsLiveData;
    }

    public String getWareHouseNameAddress(){
        DriverEntity driverEntity = RoomDBService.sharedInstance().getDatabase(context).driverDao().getDriver();
        return driverEntity.getAddressLine1()+", "+ driverEntity.getAddressLine2();
    }

    public DeliveryOrderEntity getDeliveryOrder(int doId) {
        return leepDatabase.deliveryOrderDao().getDeliveryOrder(doId);
    }

    public OrderItemEntity getDeliveryOrderItem(int doItemId) {
        return leepDatabase.deliveryOrderItemDao().getDeliveryOrderItem(doItemId);
    }

    public void confirmPickup(List<OrderItemEntity> cashDoItems, ArrayList<Integer> selectedDeliveryOrders,
                              final UILevelNetworkCallback pickupConfirmCallBack) {
        Map<String, Object> requestBody = new HashMap<>();
        try {
            requestBody.put("delivery_order_ids", selectedDeliveryOrders.toArray());
            JSONObject cashSalesObject = new JSONObject();
            cashSalesObject.put("id", cashDoItems.get(0).getDoId());
            JSONArray itemsArray = new JSONArray();
            for(OrderItemEntity orderItemEntity: cashDoItems){
                JSONObject itemObject = new JSONObject();
                itemObject.put("product_id", orderItemEntity.getProduct().getProductId());
                itemObject.put("_destroy", true);
                itemsArray.put(itemObject);
            }
            cashSalesObject.put("cash_sales_items", itemsArray);
            requestBody.put("cash_sales", cashSalesObject);
        }catch (JSONException e){
            e.printStackTrace();
        }
        if(requestBody.containsKey("delivery_order_ids") || requestBody.containsKey("cash_sales")){
            NetworkService.sharedInstance().getNetworkClient().makeJsonPutRequest(
                    getApplication().getApplicationContext(), UrlConstants.PICKUP_CONFIRMATION, true,
                    requestBody, new NetworkAPICallback() {
                        @Override
                        public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                            switch (type){
                                case NetworkConstants.SUCCESS:
                                    pickupConfirmCallBack.onResponseReceived(null,
                                            false, null, false);
                                    break;
                                case NetworkConstants.FAILURE:
                                    pickupConfirmCallBack.onResponseReceived(null,
                                            false, errorMessage, false);
                                    break;
                                case NetworkConstants.NETWORK_ERROR:
                                    pickupConfirmCallBack.onResponseReceived(null,
                                            true, errorMessage, false);
                                    break;
                                case NetworkConstants.UNAUTHORIZED:
                                    pickupConfirmCallBack.onResponseReceived(null,
                                            false, errorMessage, true);
                            }
                        }
                    });
        }

    }
}
