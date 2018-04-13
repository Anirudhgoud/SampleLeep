package com.goleep.driverapp.viewmodels.pickup.pickup;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.util.SparseArray;

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
import com.goleep.driverapp.viewmodels.dropoff.deliveryorders.DropOffDeliveryOrdersViewModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by vishalm on 27/02/18.
 */

public class PickupDeliveryOrderViewModel extends DropOffDeliveryOrdersViewModel {
    private LiveData<List<OrderItemEntity>> doDetailsLiveData = new MutableLiveData<>();

    private ArrayList<Integer> cashDoItems = new ArrayList<>();
    private ArrayList<Integer> selectedDeliveryOrders = new ArrayList<>();
    private List<OrderItemEntity> cashSalesItems = new ArrayList<>();

    private List<BaseListItem> doList = new ArrayList<>();
    private Map<Integer, Boolean> doUpdateMap = new HashMap<>();
    private SparseArray positionMap = new SparseArray();


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

    public ArrayList<Integer> getCashDoItems() {
        return cashDoItems;
    }

    public void setCashDoItems(ArrayList<Integer> cashDoItems) {
        this.cashDoItems = cashDoItems;
    }

    public ArrayList<Integer> getSelectedDeliveryOrders() {
        return selectedDeliveryOrders;
    }

    public void setSelectedDeliveryOrders(ArrayList<Integer> selectedDeliveryOrders) {
        this.selectedDeliveryOrders = selectedDeliveryOrders;
    }

    public List<OrderItemEntity> getCashSalesItems() {
        return cashSalesItems;
    }

    public void setCashSalesItems(List<OrderItemEntity> cashSalesItems) {
        this.cashSalesItems = cashSalesItems;
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

    public SparseArray getPositionMap() {
        return positionMap;
    }

    public void confirmPickup(List<OrderItemEntity> cashDoItems, ArrayList<Integer> selectedDeliveryOrders,
                              final UILevelNetworkCallback pickupConfirmCallBack) {
        Map<String, Object> requestBody = generateRequestMap(selectedDeliveryOrders, cashDoItems);
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
                                            true, errorMessage, false);
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

    private Map<String, Object> generateRequestMap(ArrayList<Integer> selectedDeliveryOrders,
                                                   List<OrderItemEntity> cashDoItems){
        Map<String, Object> requestBody = new HashMap<>();
        if(selectedDeliveryOrders.size() > 0) {
            int[] selectedDeliveryOrdersArray = new int[selectedDeliveryOrders.size()];
            for (int i=0;i < selectedDeliveryOrders.size();i++) {
                selectedDeliveryOrdersArray[i] = selectedDeliveryOrders.get(i);
            }
            requestBody.put("delivery_order_ids", selectedDeliveryOrdersArray);
        }
        if(cashDoItems.size() > 0) {
            Map<String, Object> cashSalesObject = new HashMap();
            cashSalesObject.put("id", cashDoItems.get(0).getOrderId());
            List<Map<String, Object>> orderItemMapList = new ArrayList<>();
            for (OrderItemEntity orderItemEntity : cashDoItems) {
                Map<String, Object> itemObject = new HashMap<>();
                itemObject.put("product_id", orderItemEntity.getProduct().getProductId());
                orderItemMapList.add(itemObject);
            }
            List<OrderItemEntity> unselectedCashSaleItems = leepDatabase.deliveryOrderItemDao().
                    getUnselectedOrderItems(getCashDoItems());
            for (OrderItemEntity orderItemEntity : unselectedCashSaleItems) {
                Map<String, Object> itemObject = new HashMap<>();
                itemObject.put("product_id", orderItemEntity.getProduct().getProductId());
                itemObject.put("destroy", true);
                orderItemMapList.add(itemObject);
            }
            cashSalesObject.put("cash_sales_items", orderItemMapList);
            requestBody.put("cash_sales", cashSalesObject);
        }
        return requestBody;
    }

    public void deleteDeliveryOrders(ArrayList<Integer> selectedDeliveryOrders, List<OrderItemEntity> cashSalesItems) {
        for (Integer doId : selectedDeliveryOrders)
            leepDatabase.deliveryOrderDao().deleteDeliveryOrder(doId);
        if (cashSalesItems.size() > 0)
            leepDatabase.deliveryOrderDao().deleteDeliveryOrder(cashSalesItems.get(0).getOrderId());
    }
}
