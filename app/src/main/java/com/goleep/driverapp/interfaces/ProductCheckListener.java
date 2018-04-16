package com.goleep.driverapp.interfaces;


import com.goleep.driverapp.services.room.entities.StockProductEntity;

/**
 * Created by vishalm on 16/04/18.
 */
public interface ProductCheckListener {
    void onItemChecked(StockProductEntity stockProductEntity, boolean checked, int position);
}
