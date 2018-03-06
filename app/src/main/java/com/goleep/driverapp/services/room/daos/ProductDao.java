package com.goleep.driverapp.services.room.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.goleep.driverapp.services.room.entities.ProductEntity;

import java.util.List;

/**
 * Created by vishalm on 28/02/18.
 */
@Dao
public abstract class ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllProducts(List<ProductEntity> productEntities);

//    @Query("SELECT * FROM Product WHERE doId = :doId")
//    public abstract List<Product> getAllProducts(Integer doId);
//
//    @Query("DELETE FROM Product WHERE doId = :doId")
//    public abstract List<Product> deleteAllProducts(Integer doId);

//    @Transaction
//    public void insertAndDeleteInTransaction(Integer doId, List<Product> products) {
//        deleteAllProducts(doId);
//        insertAllProducts(products);
//    }
}
