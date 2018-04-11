package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.PaymentMethod;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewSalesConfirmationViewModel extends CashSalesPaymentMethodViewModel {

    private boolean signatureAdded = false;
    private String paymentMethod;
    public String RECEIVER_SIGNATURE = "receiver_signature";

    public NewSalesConfirmationViewModel(@NonNull Application application) {
        super(application);
    }

    public void createCashSalesdeliveryOrder(String receivedBy, String contactNo, File file, final UILevelNetworkCallback deliverOrderNetworkCallBack) {
        LogUtils.debug(this.getClass().getSimpleName(), generateCashSalesRequestMap(receivedBy, contactNo).toString());

        NetworkService.sharedInstance().getNetworkClient().uploadImageWithMultipartFormData(getApplication().getApplicationContext(), UrlConstants.CREATE_CASH_SALE_DO, true, generateCashSalesRequestMap(receivedBy, contactNo), file, RECEIVER_SIGNATURE, NetworkConstants.POST_REQUEST, (type, response, errorMessage) -> {
            switch (type) {
                case NetworkConstants.SUCCESS:
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

    private Map<String, Object> generateCashSalesRequestMap(String receivedBy, String contactNo) {
        Map<String, Object> requestForm = new HashMap<>();
        requestForm.put("type", "cash_sale");
        requestForm.put("destination_location_id", consumerLocation.getId());
        requestForm.put("assignee_id", getAssigneeId());
        if (paymentCollected != 0) {
            requestForm.put("payment_collected", paymentCollected);
        }
        requestForm.put("payment_mode", PaymentMethod.CASH);
        requestForm.put("received_by", receivedBy);
        if (contactNo != null) {
            requestForm.put("receiver_contact_number", contactNo);
        }
        requestForm.put("delivery_order_items_attributes", generateProductItemsMap());
        return requestForm;
    }

    private List<Map<String, Object>> generateProductItemsMap(){
        List<Map<String, Object>> listOfMapProducts = new ArrayList<>();

        for (Product product : scannedProducts) {
            Map<String, Object> productMap = new HashMap<>();
            if (product == null) continue;
            productMap.put("product_id", product.getId());
            productMap.put("quantity", product.getQuantity());
            listOfMapProducts.add(productMap);
        }
        return listOfMapProducts;
    }


    private int getAssigneeId() {
        return LocalStorageService.sharedInstance().getLocalFileStore().getInt(getApplication().getApplicationContext(), SharedPreferenceKeys.DRIVER_ID);
    }

    //Getters and setters
    public boolean isSignatureAdded() {
        return signatureAdded;
    }

    public void setSignatureAdded(boolean signatureAdded) {
        this.signatureAdded = signatureAdded;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
