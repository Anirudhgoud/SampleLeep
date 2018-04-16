package com.goleep.driverapp.viewmodels.dropoff.cashsales;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.ReturnReasonParser;
import com.goleep.driverapp.services.room.entities.StockProductEntity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SelectReturnsProductViewModel extends CashSalesSelectProductsViewModel {

    private ArrayList<Product> selectedProducts = new ArrayList<>();
    private List<ReturnReason> returnReasons = new ArrayList<>();

    public SelectReturnsProductViewModel(@NonNull Application application) {
        super(application);
    }

    public List<StockProductEntity> allProductsContainingName(String searchText) {
        return leepDatabase.stockProductDao().allProductsWithName(searchText);
    }

    public StockProductEntity productsWithBarcode(String barcode) {
        return leepDatabase.stockProductDao().productHavingBarcode(barcode);
    }

    //Getters and setters
    public ArrayList<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(ArrayList<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public List<ReturnReason> getReturnReasons() {
        return returnReasons;
    }

    public void setReturnReasons(List<ReturnReason> returnReasons) {
        this.returnReasons = returnReasons;
    }

    public void fetchReturnReasons(final UILevelNetworkCallback reasonsCallback){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(),
                UrlConstants.RETURN_REASONS, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                ReturnReasonParser returnReasonParser = new ReturnReasonParser();
                                returnReasons = returnReasonParser.parseJsonForReturnReasons(response);
                                reasonsCallback.onResponseReceived(returnReasons, false,
                                        null, false);
                                break;
                            case NetworkConstants.FAILURE:
                                reasonsCallback.onResponseReceived(null, true,
                                        errorMessage, false);
                                break;
                            case NetworkConstants.UNAUTHORIZED:
                                reasonsCallback.onResponseReceived(null, false,
                                        null, true);
                                break;
                            case NetworkConstants.NETWORK_ERROR:
                                reasonsCallback.onResponseReceived(null, true,
                                        errorMessage, false);
                        }
                    }
                });
    }

}
