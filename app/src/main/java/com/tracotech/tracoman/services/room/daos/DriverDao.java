package com.tracotech.tracoman.services.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.sqlite.SQLiteDatabaseLockedException;

import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.DriverEntity;
import com.tracotech.tracoman.utils.LogUtils;

import java.util.List;

/**
 * Created by vishalm on 22/02/18.
 */
@Dao
public abstract class DriverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDriver(DriverEntity driverEntity);

    @Query("SELECT * FROM DriverEntity")
    public abstract DriverEntity getDriver();

    @Query("DELETE FROM DriverEntity")
    public abstract void deleteDriver();

    @Transaction
    public void updateDriver(DriverEntity driverEntity) {
        try {
            deleteDriver();
            insertDriver(driverEntity);
        }catch (SQLiteDatabaseLockedException e){
            e.printStackTrace();
            LogUtils.error("SQLiteDatabaseLockedException", "");
        }
    }

}
