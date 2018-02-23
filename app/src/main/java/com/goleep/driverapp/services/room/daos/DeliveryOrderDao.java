package com.goleep.driverapp.services.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.goleep.driverapp.services.room.entities.DeliveryOrder;

import java.util.List;

/**
 * Created by anurag on 16/02/18.
 */

@Dao
public interface DeliveryOrderDao {

    @Query("Select * from DeliveryOrder")
    LiveData<List<DeliveryOrder>> getAllDeliveryOrders();

    @Query("Select * from DeliveryOrder where status = :status and type = :type")
    LiveData<List<DeliveryOrder>> getCustomerDeliveryOrders(String type, String status);

    @Query("Delete from DeliveryOrder")
    void deleteAllDeliveryOrders();

    @Query("Select * from DeliveryOrder where id =:id")
    DeliveryOrder deliveryOrder(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDeliveryOrders(List<DeliveryOrder> deliveryOrders);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDeliveryOrder(DeliveryOrder deliveryOrder);

    @Query("Delete from DeliveryOrder WHERE type = 'driver'")
    void deleteDriverDo();

    @Query("Select * from DeliveryOrder where type = 'driver'")
    LiveData<DeliveryOrder> getDriverDo();
}
