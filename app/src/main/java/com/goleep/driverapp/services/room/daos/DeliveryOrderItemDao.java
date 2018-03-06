package com.goleep.driverapp.services.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.goleep.driverapp.services.room.entities.OrderItemEntity;

import java.util.List;

/**
 * Created by vishalm on 27/02/18.
 */
@Dao
public abstract class DeliveryOrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertDeliveryOrderItems(List<OrderItemEntity> orderItemEntities);

    @Query("SELECT * FROM OrderItemEntity WHERE doId = :id")
    public abstract LiveData<List<OrderItemEntity>> getDeliveryOrderItems(Integer id);

    @Query("DELETE FROM OrderItemEntity WHERE id = :doId")
    abstract void deleteDeliveryItems(Integer doId);

    @Query("SELECT * FROM OrderItemEntity WHERE doId = :id")
    public abstract LiveData<List<OrderItemEntity>> getDriverDoItems(Integer id);

    @Transaction
    public void deleteAndInsertItems(Integer doId, List<OrderItemEntity> items) {
        deleteDeliveryItems(doId);
        insertDeliveryOrderItems(items);
    }
}
