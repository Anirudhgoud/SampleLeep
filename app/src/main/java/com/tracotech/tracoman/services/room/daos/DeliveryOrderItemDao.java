package com.tracotech.tracoman.services.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;

import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.utils.LogUtils;

import java.util.List;

/**
 * Created by vishalm on 27/02/18.
 */
@Dao
public abstract class DeliveryOrderItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertDeliveryOrderItems(List<OrderItemEntity> orderItemEntities);

    @Query("SELECT * FROM OrderItemEntity WHERE orderId = :id")
    public abstract LiveData<List<OrderItemEntity>> getDeliveryOrderItems(int id);

    @Query("SELECT * FROM OrderItemEntity WHERE id = :itemId")
    public abstract OrderItemEntity getOrderItem(int itemId);

    @Query("SELECT * FROM OrderItemEntity WHERE orderId = :id AND selected = 1")
    public abstract List<OrderItemEntity> getSelectedOrderItems(int id);

    @Query("UPDATE OrderItemEntity SET quantity = :updatedQuantity WHERE id = :orderItemId")
    public abstract void updateOrderItemQuantity(int orderItemId, int updatedQuantity);

    @Query("UPDATE OrderItemEntity SET selected = :checked WHERE id = :orderItemId")
    public abstract void updateOrderItemSelectionStatus(int orderItemId, boolean checked);

    @Query("DELETE FROM OrderItemEntity WHERE id = :doId")
    public abstract void deleteDeliveryItems(int doId);

    @Query("SELECT * FROM OrderItemEntity WHERE orderId = :id")
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

    @Query("SELECT * FROM OrderItemEntity WHERE orderId = :doid AND id NOT IN (:cashDoItems)")
    public abstract List<OrderItemEntity> getUnselectedOrderItems(int doid, List<Integer> cashDoItems);
}
