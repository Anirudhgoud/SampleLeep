package com.goleep.driverapp.services.room;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.goleep.driverapp.services.room.daos.DeliveryOrderDao;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;

/**
 * Created by vishalm on 09/02/18.
 */
@Database(entities = {DeliveryOrder.class}, version = 1)
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
}
