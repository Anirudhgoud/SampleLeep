package com.goleep.driverapp.interfaces;

import java.util.List;

/**
 * Created by kunalsingh on 12/04/17.
 */

public interface UILevelNetworkCallback {

    void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout);

}
