package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DriverEntity;

/**
 * Created by vishalm on 21/02/18.
 */

public class PickupViewModel extends AndroidViewModel {
    Context context;
    public PickupViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

    public String getWareHouseNameAddress(){
        DriverEntity driverEntity = RoomDBService.sharedInstance().getDatabase(context).driverDao().getDriver();
        return driverEntity.getLocationName() + ", " + driverEntity.getAddressLine1() + ", " + driverEntity.getAddressLine2();
     }
}
