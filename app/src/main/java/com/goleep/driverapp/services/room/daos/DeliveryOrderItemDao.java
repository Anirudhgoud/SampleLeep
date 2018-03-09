package com.goleep.driverapp.services.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;

import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.utils.LogUtils;

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
    public abstract List<OrderItemEntity> getDOrderItemssList(Integer id);

    @Transaction
    public void deleteAndInsertItems(Integer doId, List<OrderItemEntity> items) {
        try {
            deleteDeliveryItems(doId);
            insertDeliveryOrderItems(items);
        }catch (SQLiteConstraintException e){
            e.printStackTrace();
            LogUtils.error("ForeignKey", TextUtils.join(", ", items));
        }
    }

    @Query("SELECT * FROM OrderItemEntity WHERE  id = :doItemId")
    public abstract OrderItemEntity getDeliveryOrderItem(int doItemId);
}
