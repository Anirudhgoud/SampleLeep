package com.goleep.driverapp.viewmodels.pickup.returns;

import android.app.Application;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.ReturnReasonParser;
import com.goleep.driverapp.viewmodels.dropoff.cashsales.CashSalesSelectProductsViewModel;

import java.util.List;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsSelectProductViewModel extends CashSalesSelectProductsViewModel {
    private List<ReturnReason> returnReasons;
    public ReturnsSelectProductViewModel(@NonNull Application application) {
        super(application);
    }

    public List<ReturnReason> getReturnReasons() {
        return returnReasons;
    }

    public void setReturnReasons(List<ReturnReason> returnReasons) {
        this.returnReasons = returnReasons;
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

}
