package com.goleep.driverapp.services.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.services.room.entities.UserMeta;



/**
 * Created by vishalm on 21/02/18.
 */
@Dao
public interface UserMetaDao {
    @Insert
    void insertUserMeta(UserMeta userMeta);

    @Query("SELECT * FROM UserMeta")
    UserMeta  getUserMeta();

    @Query("DELETE FROM UserMeta")
    void deleteUserMeta();
}
