package com.goleep.driverapp.services.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
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

    @Query("Select * from DeliveryOrder where status = 'in_transit' and type = 'customer'")
    LiveData<List<DeliveryOrder>> getCustomerDeliveryOrders();

    @Query("Delete from DeliveryOrder")
    void deleteAllDeliveryOrders();

    @Query("Select * from DeliveryOrder where id =:id")
    DeliveryOrder deliveryOrder(int id);

    @Insert
    void insertDeliveryOrders(List<DeliveryOrder> deliveryOrders);
}
