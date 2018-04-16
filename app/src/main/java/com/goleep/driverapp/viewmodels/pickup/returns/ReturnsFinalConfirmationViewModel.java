package com.goleep.driverapp.viewmodels.pickup.returns;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.PaymentMethod;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.LocationParser;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.utils.LogUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsFinalConfirmationViewModel extends ReturnsPaymentMethodViewModel {

    private boolean signatureAdded = false;
    private boolean paymentSkipped = false;
    private String paymentMethod;
    public String RECEIVER_SIGNATURE = "receiver_signature";

    public ReturnsFinalConfirmationViewModel(@NonNull Application application) {
        super(application);
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod(){
        return paymentMethod;
    }
    public void updateAreaInConsumerLocation(String updatedAddress) {
        if (consumerLocation != null) consumerLocation.setArea(updatedAddress);
    }

    public boolean isSignatureAdded() {
        return signatureAdded;
    }

    public void setSignatureAdded(boolean signatureAdded) {
        this.signatureAdded = signatureAdded;
    }

    public boolean isPaymentSkipped() {
        return paymentSkipped;
    }

    public void setPaymentSkipped(boolean paymentSkipped) {
        this.paymentSkipped = paymentSkipped;
    }

    public String getRECEIVER_SIGNATURE() {
        return RECEIVER_SIGNATURE;
    }

    public void setRECEIVER_SIGNATURE(String RECEIVER_SIGNATURE) {
        this.RECEIVER_SIGNATURE = RECEIVER_SIGNATURE;
    }

    public void fetchBusinessLocation(int businessId, int locationId, final UILevelNetworkCallback locationCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                UrlConstants.BUSINESS_LOCATIONS_URL + "/" + businessId + "/locations/" + locationId, true, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            LocationParser locationParser = new LocationParser();
                            Location location = locationParser.getBusinessLocation(response);
                            locationCallBack.onResponseReceived(Collections.singletonList(location), false, null, false);
                            break;

                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                            locationCallBack.onResponseReceived(null, true, errorMessage, false);
                            break;

                        case NetworkConstants.UNAUTHORIZED:
                            locationCallBack.onResponseReceived(null, false, errorMessage, true);
                            break;
                    }
                });
    }

    public void createReturnsDeliveryOrder(String receivedBy, String contactNo, File file,
                                           final UILevelNetworkCallback deliverOrderNetworkCallBack) {
        LogUtils.debug(this.getClass().getSimpleName(), generateReturnOrderRequestMap(receivedBy, contactNo).toString());
        Map<String, Object> requestMap = generateReturnOrderRequestMap(receivedBy, contactNo);
        NetworkService.sharedInstance().getNetworkClient().uploadImageWithMultipartFormData(getApplication().getApplicationContext(), UrlConstants.RETURNED_ORDERS, true, requestMap, file, RECEIVER_SIGNATURE, NetworkConstants.POST_REQUEST, (type, response, errorMessage) -> {
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

    private Map<String, Object> generateReturnOrderRequestMap(String receivedBy, String contactNo) {
        Map<String, Object> requestForm = new HashMap<>();
        requestForm.put("type", "customer");
        requestForm.put("source_location_id", consumerLocation.getId());
        requestForm.put("assignee_id", assigneeId());
        requestForm.put("payment_collected", paymentCollected);
        requestForm.put("payment_mode", PaymentMethod.CASH);
        requestForm.put("received_by", receivedBy);
        if (contactNo != null) {
            requestForm.put("receiver_contact_number", contactNo);
        }
        requestForm.put("return_order_items_attributes", new Gson().toJson(generateReturnsProductItemsMap()));
        return requestForm;
    }

    private List<Map<String, Object>> generateReturnsProductItemsMap(){
        List<Map<String, Object>> listOfMapProducts = new ArrayList<>();

        for (Product product : scannedProducts) {
            if (product == null) continue;
            if (product.getReturnQuantity() > 0){
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("product_id", product.getId());
                productMap.put("quantity", product.getReturnQuantity());
                productMap.put("return_reason_id", product.getReturnReason().getId());
                listOfMapProducts.add(productMap);
            }
        }
        return listOfMapProducts;
    }

    private int assigneeId() {
        return LocalStorageService.sharedInstance().getLocalFileStore().getInt(getApplication().getApplicationContext(), SharedPreferenceKeys.DRIVER_ID);
    }

}
