package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.PaymentMethod;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.utils.LogUtils;

import java.io.File;
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
    public String RECEIVER_SIGNATURE = "receiver_signature";

    public DropOffPaymentConfirmationViewModel(@NonNull Application application) {
        super(application);
    }

    public void editDeliveryOrderWithSelectedProducts(final UILevelNetworkCallback orderItemNetworkCallBack) {
        LogUtils.debug(this.getClass().getSimpleName(), generateOrderItemsRequestMap().toString());

        NetworkService.sharedInstance().getNetworkClient().makeJsonPutRequest(getApplication().getApplicationContext(), UrlConstants.DELIVERY_ORDERS_URL + "/" + deliveryOrderId, true, generateOrderItemsRequestMap(), (type, response, errorMessage) -> {
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

    public void deliverOrder(String receivedBy, String contactNo, File file, final UILevelNetworkCallback deliverOrderNetworkCallBack) {
        LogUtils.debug(this.getClass().getSimpleName(), generateDeliverOrderRequestMap(receivedBy, contactNo).toString());

        NetworkService.sharedInstance().getNetworkClient().uploadImageWithMultipartFormData(getApplication().getApplicationContext(), UrlConstants.DELIVER_DELIVERY_ORDER_URL, true, generateDeliverOrderRequestMap(receivedBy, contactNo), file, RECEIVER_SIGNATURE, NetworkConstants.PUT_REQUEST, (type, response, errorMessage) -> {
            switch (type) {
                case NetworkConstants.SUCCESS:
                    deleteOrderWithItems();
                    deliverOrderNetworkCallBack.onResponseReceived(new ArrayList<>(), false, null, false);
                    break;

                case NetworkConstants.FAILURE:
                case NetworkConstants.NETWORK_ERROR:
                    deliverOrderNetworkCallBack.onResponseReceived(null, true, errorMessage, false);
                    break;

                case NetworkConstants.UNAUTHORIZED:
                    deliverOrderNetworkCallBack.onResponseReceived(null,
                            false, errorMessage, true);
                    break;
            }
        });
    }

    private void deleteOrderWithItems() {
        leepDatabase.deliveryOrderDao().deleteDeliveryOrder(deliveryOrderId);
        leepDatabase.deliveryOrderItemDao().deleteDeliveryItems(deliveryOrderId);
    }

    private int getAssigneeId() {
        return LocalStorageService.sharedInstance().getLocalFileStore().getInt(getApplication().getApplicationContext(), SharedPreferenceKeys.DRIVER_ID);
    }


    private Map<String, Object> generateOrderItemsRequestMap() {
        Map<String, Object> httpBodyDetails = new HashMap<>();
        httpBodyDetails.put("delivery_order_items_attributes", getDeliveryOrderItemsMap());
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

    private Map<String, Object> generateDeliverOrderRequestMap(String receivedBy, String contactNo) {
        Map<String, Object> requestForm = new HashMap<>();
        requestForm.put("delivery_order_id", deliveryOrderId);
        if (paymentCollected != 0) {
            requestForm.put("payment_collected", paymentCollected);
        }
        requestForm.put("payment_mode", PaymentMethod.CASH);
        requestForm.put("received_by", receivedBy);
        if (contactNo != null) {
            requestForm.put("receiver_contact_number", contactNo);
        }
        return requestForm;
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
