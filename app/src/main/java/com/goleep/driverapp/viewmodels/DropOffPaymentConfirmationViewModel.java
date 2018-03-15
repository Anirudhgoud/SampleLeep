package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anurag on 13/03/18.
 */

public class DropOffPaymentConfirmationViewModel extends DropOffDoBaseViewModel {

    private String businessAddress;
    private double currentSale;
    private double outstandingBalance;
    private double paymentCollected;
    private double grandTotal;
    private double previousBalance;
    private String paymentMethod;
    private boolean signatureAdded = false;

    public DropOffPaymentConfirmationViewModel(@NonNull Application application) {
        super(application);
    }

    public void editDeliveryOrderWithSelectedProducts(final UILevelNetworkCallback orderItemNetworkCallBack) {
        LogUtils.debug(this.getClass().getSimpleName(), generateOrderItemsRequestMap().toString());

        NetworkService.sharedInstance().getNetworkClient().makePutRequest(getApplication().getApplicationContext(), UrlConstants.DELIVERY_ORDERS_URL + "/" + deliveryOrderId, true, generateOrderItemsRequestMap(), (type, response, errorMessage) -> {
            switch (type) {
                case NetworkConstants.SUCCESS:
                    orderItemNetworkCallBack.onResponseReceived(new ArrayList<>(), false, null, false);
                    break;

                case NetworkConstants.FAILURE:
                case NetworkConstants.NETWORK_ERROR:
                    orderItemNetworkCallBack.onResponseReceived(null, true, errorMessage, false);
                    break;

                case NetworkConstants.UNAUTHORIZED:
                    orderItemNetworkCallBack.onResponseReceived(null,
                            false, errorMessage, true);
                    break;
            }
        });
    }

    private int getAssigneeId() {
        return LocalStorageService.sharedInstance().getLocalFileStore().getInt(getApplication().getApplicationContext(), SharedPreferenceKeys.DRIVER_ID);
    }


    private Map<String, Object> generateOrderItemsRequestMap() {
        Map<String, Object> httpBodyDetails = new HashMap<>();
        httpBodyDetails.put("delivery_order_items", getDeliveryOrderItemsMap());
        httpBodyDetails.put("assignee_id", getAssigneeId());

        Map<String, Object> httpBody = new HashMap<>();
        httpBody.put("delivery_order", httpBodyDetails);
        return httpBody;
    }

    private List<Map<String, Object>> getDeliveryOrderItemsMap() {
        List<OrderItemEntity> orderItems = leepDatabase.deliveryOrderItemDao().getDOrderItemssList(deliveryOrderId);
        List<Map<String, Object>> orderItemMapList = new ArrayList<>();
        for (OrderItemEntity orderItem : orderItems) {
            Map<String, Object> orderItemMap = new HashMap<>();
            orderItemMap.put("quantity", orderItem.getQuantity());
            orderItemMap.put("product_id", orderItem.getProduct().getProductId());
            orderItemMap.put("id", orderItem.getId());
            orderItemMap.put("_destroy", !orderItem.isSelected());
            orderItemMapList.add(orderItemMap);
        }
        return orderItemMapList;
    }

    //Getters and setters
    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public double getCurrentSale() {
        return currentSale;
    }

    public void setCurrentSale(double currentSale) {
        this.currentSale = currentSale;
    }

    public double getOutstandingBalance() {
        return getGrandTotal() - paymentCollected;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public double getPaymentCollected() {
        return paymentCollected;
    }

    public void setPaymentCollected(double paymentCollected) {
        this.paymentCollected = paymentCollected;
    }

    public double getGrandTotal() {
        return currentSale + previousBalance;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isSignatureAdded() {
        return signatureAdded;
    }

    public void setSignatureAdded(boolean signatureAdded) {
        this.signatureAdded = signatureAdded;
    }
}
