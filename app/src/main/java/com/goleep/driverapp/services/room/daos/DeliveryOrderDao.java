package com.goleep.driverapp.services.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.text.TextUtils;


import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.utils.LogUtils;


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

    @Query("Delete from DeliveryOrderEntity")
    public abstract void deleteAllDeliveryOrders();

    @Query("Select * from DeliveryOrderEntity where id =:id")
    public abstract DeliveryOrderEntity getDeliveryOrder(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDeliveryOrders(List<DeliveryOrderEntity> deliveryOrders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertDeliveryOrder(DeliveryOrderEntity deliveryOrder);

    @Query("Delete from DeliveryOrderEntity WHERE type = 'driver'")
    public abstract void deleteDriverDo();

    @Query("Select * from DeliveryOrderEntity where type = 'driver'")
    public abstract LiveData<DeliveryOrderEntity> getDriverDo();

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
