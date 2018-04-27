package com.goleep.driverapp.services.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.sqlite.SQLiteDatabaseLockedException;

import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;
import com.goleep.driverapp.utils.LogUtils;

import java.util.List;

/**
 * Created by vishalm on 22/03/18.
 */
@Dao
public abstract class ReturnOrderDao {
    @Transaction
    public void updateAllDeliveryOrders(List<ReturnOrderEntity> returnOrderEntities) {
        try {
            deleteAllReturnOrders();
            insertAllReturnOrders(returnOrderEntities);
        }catch (SQLiteDatabaseLockedException e){
            e.printStackTrace();
            LogUtils.error("SQLiteDatabaseLockedException", "");
        }
    }

    @Query("DELETE FROM ReturnOrderEntity")
    public abstract void deleteAllReturnOrders();

    @Insert
    public abstract void insertAllReturnOrders(List<ReturnOrderEntity> returnOrderEntities);

    @Query("SELECT * FROM ReturnOrderEntity WHERE roNumber = :roNumber")
    public abstract ReturnOrderEntity getReturnOrderEntity(long roNumber);

}
