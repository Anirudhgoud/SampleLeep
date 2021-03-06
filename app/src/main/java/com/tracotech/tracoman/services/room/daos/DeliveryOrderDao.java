package com.tracotech.tracoman.services.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.sqlite.SQLiteDatabaseLockedException;

import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.utils.LogUtils;

import java.util.List;

/**
 * Created by anurag on 16/02/18.
 */

@Dao
public abstract class DeliveryOrderDao {

    @Query("Select * from DeliveryOrderEntity")
    public abstract LiveData<List<DeliveryOrderEntity>> getAllDeliveryOrders();

    @Query("Select * from DeliveryOrderEntity where status = :status and type = :type")
    public abstract LiveData<List<DeliveryOrderEntity>> getCustomerDeliveryOrders(String type, String status);

    @Query("Select * from DeliveryOrderEntity where status = :status and type = :type and sourceLocationId = :sourceLocationId")
    public abstract LiveData<List<DeliveryOrderEntity>> getCustomerDeliveryOrders(String type, String status, int sourceLocationId);

    @Query("Delete from DeliveryOrderEntity")
    public abstract void deleteAllDeliveryOrders();

    @Query("Select * from DeliveryOrderEntity where id =:id")
    public abstract DeliveryOrderEntity deliveryOrder(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDeliveryOrders(List<DeliveryOrderEntity> deliveryOrders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDeliveryOrder(DeliveryOrderEntity deliveryOrder);

    @Query("Delete from DeliveryOrderEntity WHERE type = 'driver'")
    public abstract void deleteDriverDo();

    @Query("Delete from DeliveryOrderEntity WHERE id =:id")
    public abstract void deleteDeliveryOrder(int id);

    @Query("Select * from DeliveryOrderEntity where type = 'driver' and status = 'assigned' and sourceLocationId = :warehouseId")
    public abstract LiveData<DeliveryOrderEntity> getDriverDo(int warehouseId);

    @Transaction
    public void updateAllDeliveryOrders(List<DeliveryOrderEntity> deliveryOrderEntities) {
        try {
            deleteAllDeliveryOrders();
            insertDeliveryOrders(deliveryOrderEntities);
        }catch (SQLiteDatabaseLockedException e){
            e.printStackTrace();
            LogUtils.error("SQLiteDatabaseLockedException", "");
        }
    }
}
