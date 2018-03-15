package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by vishalm on 12/03/18.
 */

public class PickupMapViewModel extends AndroidViewModel {
    private AppDatabase leepDatabase;

    public PickupMapViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public String getWareHouseNameAddress() {
        DriverEntity driverEntity = RoomDBService.sharedInstance().getDatabase(
                this.getApplication().getApplicationContext()).driverDao().getDriver();
        return driverEntity.getLocationName() + ", " + driverEntity.getAddressLine1() + ", " + driverEntity.getAddressLine2();
    }

    public String getWareHouseName() {
        DriverEntity driverEntity = RoomDBService.sharedInstance().getDatabase(
                this.getApplication().getApplicationContext()).driverDao().getDriver();
        return driverEntity.getLocationName();
    }

    public LatLng getWarehouseLatLng() {
        DriverEntity driverEntity = leepDatabase.driverDao().getDriver();
        if (driverEntity.getWorkLocationLat() != 0.0 || driverEntity.getWorkLocationLng() != 0.0)
            return new LatLng(driverEntity.getWorkLocationLat(),
                    driverEntity.getWorkLocationLng());
        return null;
    }

    public String getWarehouseAddress() {
        DriverEntity driverEntity = RoomDBService.sharedInstance().getDatabase(
                this.getApplication().getApplicationContext()).driverDao().getDriver();
        return driverEntity.getAddressLine1() + ", " + driverEntity.getAddressLine2() + ", " +
                driverEntity.getCity() + ", " + driverEntity.getState() + " - " + driverEntity.getPinCode();
    }
}
