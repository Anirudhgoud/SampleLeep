package com.tracotech.tracoman.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.services.room.AppDatabase;
import com.tracotech.tracoman.services.room.RoomDBService;
import com.tracotech.tracoman.services.room.entities.WarehouseEntity;
import com.tracotech.tracoman.utils.StringUtils;

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

     public WarehouseEntity getWarehouse(){
         return warehouseEntity;
     }

    public void setWarehouse(int warehouseId) {
        warehouseEntity = leepDatabase.warehouseDao().getWarehouse(warehouseId);
    }
}
