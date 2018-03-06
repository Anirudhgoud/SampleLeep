package com.goleep.driverapp.services.room;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;

import com.goleep.driverapp.services.room.daos.DeliveryOrderDao;
import com.goleep.driverapp.services.room.daos.DeliveryOrderItemDao;
import com.goleep.driverapp.services.room.daos.DriverDao;
import com.goleep.driverapp.services.room.daos.ProductDao;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ProductEntity;

/**
 * Created by vishalm on 09/02/18.
 */
@Database(entities = {DeliveryOrderEntity.class, DriverEntity.class,
        OrderItemEntity.class, ProductEntity.class}, version = 1)
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
    public abstract ProductDao productDao();
}
