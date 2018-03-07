package com.goleep.driverapp.viewmodels;


import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by anurag on 14/02/18.
 */

public class DropOffDeliveryOrdersViewModel extends DeliveryOrderViewModel {

    private Context context;


    public DropOffDeliveryOrdersViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }


}
