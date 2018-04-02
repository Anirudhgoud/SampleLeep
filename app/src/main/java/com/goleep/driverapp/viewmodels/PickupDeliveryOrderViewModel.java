package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.OrderItemParser;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;

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
    private LiveData<List<OrderItemEntity>> doDetailsLiveData = new MutableLiveData<>();

    private List<BaseListItem> doList = new ArrayList<>();
    private Map<Integer, Boolean> doUpdateMap = new HashMap<>();
    private Map<Integer, Integer> positionMap = new HashMap<>();


    public PickupDeliveryOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchDoItems(final int doId, Observer<List<OrderItemEntity>> orderItemsObserver){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(),
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true,
                new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                OrderItemParser orderItemParser = new OrderItemParser();
                                List<OrderItemEntity> orderItems = orderItemParser.orderItemsByParsingJsonResponse(response, doId);
                                leepDatabase.deliveryOrderItemDao().deleteAndInsertItems(doId, orderItems);
                                orderItemsObserver.onChanged(orderItems);
                                break;
                        }
                    }
                });
    }

    public List<OrderItemEntity> getOrderItemsList(int doId) {
        return leepDatabase.deliveryOrderItemDao().getDOrderItemssList(doId);
    }

    public LiveData<List<OrderItemEntity>> getDoDetails(Integer id) {
        doDetailsLiveData = leepDatabase.deliveryOrderItemDao().getDeliveryOrderItems(id);
        return doDetailsLiveData;
    }

    public LiveData<List<OrderItemEntity>> getOrderItemsLiveData(){
        return doDetailsLiveData;
    }

    public String getWareHouseNameAddress(){
        DriverEntity driverEntity = leepDatabase.driverDao().getDriver();
        return driverEntity.getLocationName() + ", " + driverEntity.getAddressLine1() + ", " + driverEntity.getAddressLine2();
    }

    public DeliveryOrderEntity getDeliveryOrder(int doId) {
        return leepDatabase.deliveryOrderDao().deliveryOrder(doId);
    }

    public OrderItemEntity getDeliveryOrderItem(int doItemId) {
        return leepDatabase.deliveryOrderItemDao().getDeliveryOrderItem(doItemId);
    }

    public List<BaseListItem> getDoList() {
        return doList;
    }

    public void setDoList(List<BaseListItem> doList) {
        this.doList = doList;
    }

    public Map<Integer, Boolean> getDoUpdateMap() {
        return doUpdateMap;
    }

    public void setDoUpdateMap(Map<Integer, Boolean> doUpdateMap) {
        this.doUpdateMap = doUpdateMap;
    }

    public Map<Integer, Integer> getPositionMap() {
        return positionMap;
    }

    public void setPositionMap(Map<Integer, Integer> positionMap) {
        this.positionMap = positionMap;
    }

    public void confirmPickup(List<OrderItemEntity> cashDoItems, ArrayList<Integer> selectedDeliveryOrders,
                              final UILevelNetworkCallback pickupConfirmCallBack) {
        Map<String, Object> requestBody = new HashMap<>();
        try {
            JSONArray selectedDeliveryOrdersArray = new JSONArray();
            for (Integer doId : selectedDeliveryOrders) {
                selectedDeliveryOrdersArray.put(doId);
            }
            requestBody.put("delivery_order_ids", selectedDeliveryOrdersArray);
            if(cashDoItems.size() > 0) {
                JSONObject cashSalesObject = new JSONObject();
                cashSalesObject.put("id", cashDoItems.get(0).getOrderId());
                JSONArray itemsArray = new JSONArray();
                for (OrderItemEntity orderItemEntity : cashDoItems) {
                    JSONObject itemObject = new JSONObject();
                    itemObject.put("product_id", orderItemEntity.getProduct().getProductId());
                    itemObject.put("_destroy", false);
                    itemsArray.put(itemObject);
                }
                cashSalesObject.put("cash_sales_items", itemsArray);
                requestBody.put("cash_sales", cashSalesObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (requestBody.containsKey("delivery_order_ids") || requestBody.containsKey("cash_sales")) {
            NetworkService.sharedInstance().getNetworkClient().makeJsonPutRequest(
                    getApplication().getApplicationContext(), UrlConstants.PICKUP_CONFIRMATION, true,
                    requestBody, new NetworkAPICallback() {
                        @Override
                        public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                            switch (type) {
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

    public void deleteDeliveryOrders(ArrayList<Integer> selectedDeliveryOrders, List<OrderItemEntity> cashSalesItems) {
        for (Integer doId : selectedDeliveryOrders)
            leepDatabase.deliveryOrderDao().deleteDeliveryOrder(doId);
        if (cashSalesItems.size() > 0)
            leepDatabase.deliveryOrderDao().deleteDeliveryOrder(cashSalesItems.get(0).getOrderId());
    }
}
