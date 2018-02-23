package com.goleep.driverapp.services.room.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.goleep.driverapp.services.room.entities.DoDetails;

import java.util.List;

/**
 * Created by vishalm on 22/02/18.
 */
@Dao
public interface DoDetailsDao {
    @Insert
    void insertDoDetails(List<DoDetails> doDetailsList) ;

    @Insert
    void insertDoDetails(DoDetails doDetailsList) ;

    @Query("DELETE FROM DoDetails WHERE id = :doId")
    void deleteDoDetails(String doId);

    @Query("SELECT * FROM DoDetails WHERE type = 'driver'")
    LiveData<DoDetails> getDriverDo();

    @Query("DELETE FROM DoDetails WHERE type = 'driver'")
    void deleteDriverDo();

    @Query("SELECT * FROM DoDetails WHERE id = :id")
    LiveData<DoDetails> getDoDetails(Integer id);
}
