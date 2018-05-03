package com.tracotech.tracoman.viewmodels.pickup.pickup;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.helpers.uimodels.BaseListItem;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.services.network.jsonparsers.OrderItemParser;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.services.room.entities.WarehouseEntity;
import com.tracotech.tracoman.utils.LogUtils;
import com.tracotech.tracoman.viewmodels.dropoff.deliveryorders.DropOffDeliveryOrdersViewModel;

import java.util.ArrayList;
import java.util.Collections;
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
    private WarehouseEntity warehouseEntity;


    public PickupDeliveryOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchDoItems(final int doId, Observer<List<OrderItemEntity>> orderItemsObserver){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(),
                UrlConstants.DELIVERY_ORDERS_URL + "/" + doId, true,
                (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            OrderItemParser orderItemParser = new OrderItemParser();
                            List<OrderItemEntity> orderItems = orderItemParser.orderItemsByParsingJsonResponse(response, doId);
                            leepDatabase.deliveryOrderItemDao().deleteAndInsertItems(doId, orderItems);
                            orderItemsObserver.onChanged(orderItems);
                            break;
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

    public void setWarehouse(int warehouseId) {
        warehouseEntity = leepDatabase.warehouseDao().getWarehouse(warehouseId);
        System.out.print("");
    }

    public WarehouseEntity getWarehouse(){
        return warehouseEntity;
    }

    public String getWareHouseNameAddress(){
        return warehouseEntity.getWareHouseName() + ", " + warehouseEntity.getAddressLine1() + ", " + warehouseEntity.getAddressLine2();
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
                    requestBody, (type, response, errorMessage) -> {
                        switch (type) {
                            case NetworkConstants.SUCCESS:
                                pickupConfirmCallBack.onResponseReceived(Collections.emptyList(),
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
            int doId = cashDoItems.get(0).getOrderId();
            cashSalesObject.put("id", cashDoItems.get(0).getOrderId());
            List<Map<String, Object>> orderItemMapList = new ArrayList<>();
            for (OrderItemEntity orderItemEntity : cashDoItems) {
                Map<String, Object> itemObject = new HashMap<>();
                itemObject.put("product_id", orderItemEntity.getProduct().getProductId());
                orderItemMapList.add(itemObject);
            }
            List<OrderItemEntity> unselectedCashSaleItems = leepDatabase.deliveryOrderItemDao().
                    getUnselectedOrderItems(doId, getCashDoItems());
            for (OrderItemEntity orderItemEntity : unselectedCashSaleItems) {
                Map<String, Object> itemObject = new HashMap<>();
                itemObject.put("product_id", orderItemEntity.getProduct().getProductId());
                itemObject.put("_destroy", true);
                orderItemMapList.add(itemObject);
            }
            cashSalesObject.put("cash_sales_items", orderItemMapList);
            requestBody.put("cash_sales", cashSalesObject);
        }
        LogUtils.error("Request", requestBody.toString());
        return requestBody;
    }

    public void deleteDeliveryOrders(ArrayList<Integer> selectedDeliveryOrders, List<OrderItemEntity> cashSalesItems) {
        for (Integer doId : selectedDeliveryOrders)
            leepDatabase.deliveryOrderDao().deleteDeliveryOrder(doId);
        if (cashSalesItems.size() > 0)
            leepDatabase.deliveryOrderDao().deleteDeliveryOrder(cashSalesItems.get(0).getOrderId());
    }
}
