package com.tracotech.tracoman.services.room;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

import com.tracotech.tracoman.services.room.daos.DeliveryOrderDao;
import com.tracotech.tracoman.services.room.daos.DeliveryOrderItemDao;
import com.tracotech.tracoman.services.room.daos.DriverDao;
import com.tracotech.tracoman.services.room.daos.ReturnOrderDao;
import com.tracotech.tracoman.services.room.daos.StockProductDao;
import com.tracotech.tracoman.services.room.daos.WarehouseDao;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.DriverEntity;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.services.room.entities.ProductEntity;
import com.tracotech.tracoman.services.room.entities.ReturnOrderEntity;
import com.tracotech.tracoman.services.room.entities.StockProductEntity;
import com.tracotech.tracoman.services.room.entities.WarehouseEntity;

/**
 * Created by vishalm on 09/02/18.
 */
@Database(entities = {DeliveryOrderEntity.class, DriverEntity.class,
        OrderItemEntity.class, ProductEntity.class, StockProductEntity.class, ReturnOrderEntity.class,
        WarehouseEntity.class},
        version = 1)
//@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase{

    AppDatabase(){

    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    public abstract DeliveryOrderDao deliveryOrderDao();
    public abstract DriverDao driverDao();
    public abstract DeliveryOrderItemDao deliveryOrderItemDao();

    public abstract StockProductDao stockProductDao();
    public abstract ReturnOrderDao returnOrderDao();
    public abstract WarehouseDao warehouseDao();
}
