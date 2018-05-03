package com.tracotech.tracoman.viewmodels.dropoff.cashsales;

import android.app.Application;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.UrlConstants;
import com.tracotech.tracoman.helpers.uimodels.Product;
import com.tracotech.tracoman.helpers.uimodels.ReturnReason;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.services.network.jsonparsers.ReturnReasonParser;

import java.util.ArrayList;
import java.util.List;

public class SelectReturnsProductViewModel extends CashSalesSelectProductsViewModel {

    private ArrayList<Product> selectedProducts = new ArrayList<>();
    private List<ReturnReason> returnReasons = new ArrayList<>();

    public SelectReturnsProductViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchReturnReasons(final UILevelNetworkCallback reasonsCallback){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(),
                UrlConstants.RETURN_REASONS, true, (type, response, errorMessage) -> {
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
                });
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
}
