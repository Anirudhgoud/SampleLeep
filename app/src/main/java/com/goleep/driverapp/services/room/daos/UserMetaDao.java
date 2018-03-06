package com.goleep.driverapp.services.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.goleep.driverapp.services.room.entities.UserEntity;


/**
 * Created by vishalm on 21/02/18.
 */
@Dao
public interface UserMetaDao {
    @Insert
    void insertUserMeta(UserEntity userEntity);

    @Query("SELECT * FROM UserEntity")
    UserEntity getUserMeta();

    @Query("DELETE FROM UserEntity")
    void deleteUserMeta();
}
