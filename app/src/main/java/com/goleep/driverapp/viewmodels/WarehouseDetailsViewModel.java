package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;
import com.goleep.driverapp.utils.StringUtils;

/**
 * Created by vishalm on 21/02/18.
 */

public class WarehouseDetailsViewModel extends AndroidViewModel {
    protected AppDatabase leepDatabase;
    private WarehouseEntity warehouseEntity;
    public WarehouseDetailsViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public String getWareHouseNameAddress(){
        if(warehouseEntity == null){
            return "";
        }
        return warehouseEntity.getWareHouseName() + ", "+ StringUtils.getAddress(
                warehouseEntity.getAddressLine1(), warehouseEntity.getAddressLine2());
     }

     public WarehouseEntity getWarehouse(int warehouseId){
         warehouseEntity = leepDatabase.warehouseDao().getWarehouse(warehouseId);
        return warehouseEntity;
     }

     public WarehouseEntity getWarehouse(){
         return warehouseEntity;
     }

    public void setWarehouse(WarehouseEntity warehouse) {
        warehouseEntity = warehouse;
    }
}
