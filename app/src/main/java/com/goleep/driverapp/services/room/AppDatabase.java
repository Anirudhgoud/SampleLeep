package com.goleep.driverapp.services.room;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.goleep.driverapp.services.room.daos.DeliveryOrderDao;
import com.goleep.driverapp.services.room.daos.DoDetailsDao;
import com.goleep.driverapp.services.room.daos.DriverDao;
import com.goleep.driverapp.services.room.daos.UserMetaDao;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.services.room.entities.DoDetails;
import com.goleep.driverapp.services.room.entities.Driver;
import com.goleep.driverapp.services.room.entities.UserMeta;

/**
 * Created by vishalm on 09/02/18.
 */
@Database(entities = {DeliveryOrder.class, UserMeta.class, Driver.class, DoDetails.class}, version = 1)
@TypeConverters({Converters.class})
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
    public abstract UserMetaDao userMetaDao();
    public abstract DriverDao driverDao();
    public abstract DoDetailsDao doDetailsDao();
}
