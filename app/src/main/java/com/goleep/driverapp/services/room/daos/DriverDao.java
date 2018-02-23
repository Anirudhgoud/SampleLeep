package com.goleep.driverapp.services.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.goleep.driverapp.services.room.entities.Driver;

/**
 * Created by vishalm on 22/02/18.
 */
@Dao
public interface DriverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDriver(Driver driver);

    @Query("SELECT * FROM Driver")
    Driver getDriver();
}
