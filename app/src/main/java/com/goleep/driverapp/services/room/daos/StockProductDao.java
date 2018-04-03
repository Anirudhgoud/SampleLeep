package com.goleep.driverapp.services.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.database.sqlite.SQLiteDatabaseLockedException;

import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.utils.LogUtils;

import java.util.List;

/**
 * Created by vishalm on 19/03/18.
 */
@Dao
public abstract class StockProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllStockProducts(List<StockProductEntity> stockProductEntities);


    @Transaction
    public void updateAllDeliveryOrders(List<StockProductEntity> stockProductEntities) {
        try {
            deleteAllStockProducts();
            insertAllStockProducts(stockProductEntities);
        } catch (SQLiteDatabaseLockedException e) {
            e.printStackTrace();
            LogUtils.error("SQLiteDatabaseLockedException", "");
        }
    }

    @Query("Select * from StockProductEntity WHERE sellableQuantity != 0 AND barcode =:barcode")
    public abstract StockProductEntity sellableProductHavingBarcode(String barcode);

    @Query("Select * from StockProductEntity WHERE sellableQuantity != 0 AND productName LIKE '%' || :text  || '%'")
    public abstract List<StockProductEntity> sellebleProductsWithName(String text);

    @Query("DELETE FROM StockProductEntity")
    protected abstract void deleteAllStockProducts();

    @Query("SELECT * FROM StockProductEntity WHERE sellableQuantity != 0")
    public abstract List<StockProductEntity> getSellableStocks();

    @Query("SELECT * FROM StockProductEntity WHERE deliverableQuantity != 0")
    public abstract List<StockProductEntity> getDeliverableStocks();

    @Query("SELECT * FROM StockProductEntity WHERE returnableQuantity != 0")
    public abstract List<StockProductEntity> getReturnedStocks();
}
