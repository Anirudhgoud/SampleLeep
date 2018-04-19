package com.goleep.driverapp.viewmodels.dropoff.dropoff;

import android.app.Application;
import android.support.annotation.NonNull;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.viewmodels.WarehouseDetailsViewModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vishalm on 03/04/18.
 */

public class DropoffConfirmationViewModel extends WarehouseDetailsViewModel {

    private List<Integer> selectedReturnableIds = new ArrayList<>();
    private List<Integer> selectedSellableIds = new ArrayList<>();

    public DropoffConfirmationViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Integer> getSelectedReturnableIds() {
        return selectedReturnableIds;
    }

    public void setSelectedReturnableIds(List<Integer> selectedReturnableIds) {
        this.selectedReturnableIds = selectedReturnableIds;
    }

    public List<Integer> getSelectedSellableIds() {
        return selectedSellableIds;
    }

    public void setSelectedSellableIds(List<Integer> selectedSellableIds) {
        this.selectedSellableIds = selectedSellableIds;
    }
    public StockProductEntity getStockProduct(int id){
        return leepDatabase.stockProductDao().getStock(id);
    }

    public void confirmDropoff(final UILevelNetworkCallback dropoffConfirmCallBack) {
        Map<String, Object> requestBody = generateRequestMap();
        NetworkService.sharedInstance().getNetworkClient().uploadImageWithMultipartFormData(
                getApplication().getApplicationContext(), UrlConstants.RETURNED_ORDERS, true,
                requestBody, null, null, NetworkConstants.POST_REQUEST,new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type) {
                            case NetworkConstants.SUCCESS:
                                dropoffConfirmCallBack.onResponseReceived(new ArrayList<>(),
                                        false, null, false);
                                break;
                            case NetworkConstants.FAILURE:
                                dropoffConfirmCallBack.onResponseReceived(null,
                                        true, errorMessage, false);
                                break;
                            case NetworkConstants.NETWORK_ERROR:
                                dropoffConfirmCallBack.onResponseReceived(null,
                                        true, errorMessage, false);
                                break;
                            case NetworkConstants.UNAUTHORIZED:
                                dropoffConfirmCallBack.onResponseReceived(null,
                                        false, errorMessage, true);
                        }
                    }
                });

    }

    private Map<String, Object> generateRequestMap() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("type", "driver");
        requestMap.put("assignee_id", leepDatabase.driverDao().getDriver().getId());
        requestMap.put("destination_location_id", getWarehouse().getId());
        try {
            requestMap.put("return_order_items_attributes", new Gson().toJson(generateReturnOrdersJson()));
        }catch (JSONException e){
            System.out.print("");
        }
        return requestMap;
    }

    private Object generateReturnOrdersJson() throws JSONException {
        List<HashMap<String,Object>> returnItemsArray = new ArrayList<>();
        if(selectedReturnableIds != null) {
            int length = selectedReturnableIds.size();
            for (int i = 0; i < length; i++) {
                StockProductEntity product = getStockProduct(selectedReturnableIds.get(i));
                HashMap<String, Object> productJSON = new HashMap<>();
                productJSON.put("product_id", product.getId());
                productJSON.put("quantity", product.getQuantity(AppConstants.TYPE_RETURNED));
                productJSON.put("type", "returnable");
                returnItemsArray.add(productJSON);
            }
        }
        if(selectedSellableIds != null) {
            int length = selectedSellableIds.size();
            for (int i = 0; i < length; i++) {
                StockProductEntity product = getStockProduct(selectedSellableIds.get(i));
                HashMap<String, Object> productJSON = new HashMap<>();
                productJSON.put("product_id", product.getId());
                productJSON.put("quantity", product.getQuantity(AppConstants.TYPE_SELLABLE));
                productJSON.put("type", "sellable");
                returnItemsArray.add(productJSON);
            }
        }
        return returnItemsArray;
    }
}
