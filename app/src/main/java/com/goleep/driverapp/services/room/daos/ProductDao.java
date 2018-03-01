package com.goleep.driverapp.services.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.goleep.driverapp.services.room.entities.Product;

import java.util.List;

/**
 * Created by vishalm on 28/02/18.
 */
@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllProducts(List<Product> products);
}
