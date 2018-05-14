package com.tracotech.tracoman.viewmodels.onboarding;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.constants.NetworkConstants;
import com.tracotech.tracoman.constants.RequestConstants;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.services.network.NetworkService;
import com.tracotech.tracoman.utils.AppUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.tracotech.tracoman.constants.UrlConstants.FORGOT_PASSWORD_URL;

/**
 * Created by vishalm on 14/02/18.
 */

public class ForgotPasswordViewModel extends AndroidViewModel {

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public void submitEmail(String text, final UILevelNetworkCallback submitEmailCallback) {
        NetworkService.sharedInstance().getNetworkClient().makeJsonPostRequest(getApplication(), FORGOT_PASSWORD_URL,
                false, generateBodyParams(text), (type, response, errorMessage) -> {
                    switch (type){
                        case NetworkConstants.SUCCESS:
                            submitEmailCallback.onResponseReceived(Collections.emptyList(), false, null, false);
                            break;
                        case NetworkConstants.FAILURE:
                        case NetworkConstants.NETWORK_ERROR:
                        case NetworkConstants.UNAUTHORIZED:
                            submitEmailCallback.onResponseReceived(null, true, errorMessage, false);
                            break;
                    }
                });
    }

    private Map<String, Object> generateBodyParams(String text){
        Map<String, Object> bodyParams = new HashMap<>();
        Map<String, Object> innerParams = new HashMap<>();
        if (text.matches(AppUtils.EMAIL_PATTERN))
            innerParams.put(RequestConstants.KEY_EMAIL, text);
        else if (text.matches(AppUtils.PHONE_PATTERN))
            innerParams.put(RequestConstants.KEY_PHONE_NUMBER, text);
        bodyParams.put(RequestConstants.KEY_USER, innerParams);
        return bodyParams;
    }
}
