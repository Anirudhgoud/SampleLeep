package com.goleep.driverapp.services.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.goleep.driverapp.services.room.entities.WarehouseEntity;

import java.util.List;

/**
 * Created by vishalm on 29/03/18.
 */
@Dao
public abstract class WarehouseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public  abstract void insertWarehouseEntities(List<WarehouseEntity> warehouseEntities) ;

    @Query("SELECT * FROM WAREHOUSEENTITY")
    public abstract List<WarehouseEntity> getAllWarehouses();

    @Query("SELECT * FROM WarehouseEntity where id = :warehouseId")
    public abstract WarehouseEntity getWarehouse(int warehouseId);
}
