package com.goleep.driverapp.services.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;

import java.util.List;

/**
 * Created by vishalm on 27/02/18.
 */
@Dao
public interface DeliveryOrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDeliveryOrderItems(List<DeliveryOrderItem> deliveryOrderItems);

    @Query("SELECT * FROM DeliveryOrderItem WHERE doId = :id")
    LiveData<List<DeliveryOrderItem>> getDeliveryOrderItems(Integer id);



    @Query("DELETE FROM DeliveryOrderItem WHERE id = :doId")
    void deleteDeliveryItems(Integer doId);

    @Query("SELECT * FROM DeliveryOrderItem WHERE doId = :id")
    LiveData<List<DeliveryOrderItem>> getDriverDoItems(Integer id);
}
