package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.ReturnReasonParser;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by vishalm on 05/04/18.
 */

public class ReturnReasonsViewModel extends AndroidViewModel {

    public ReturnReasonsViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchReturnReasons(final UILevelNetworkCallback reasonsCallback){
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(),
                UrlConstants.RETURN_REASONS, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                ReturnReasonParser returnReasonParser = new ReturnReasonParser();
                                List<ReturnReason> returnReasons = returnReasonParser.parseJsonForReturnReasons(response);
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
