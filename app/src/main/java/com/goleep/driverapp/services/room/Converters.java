package com.goleep.driverapp.services.room;

import android.arch.persistence.room.TypeConverter;

import com.goleep.driverapp.services.room.entities.ProductEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by vishalm on 21/02/18.
 */

public class Converters {
    @TypeConverter
    public static ProductEntity productFromString(String value){
        Type type = new TypeToken<ProductEntity>(){}.getType();
        return new Gson().fromJson(value, type);
    }
    @TypeConverter
    public static String stringFromProduct(ProductEntity products){
        return new Gson().toJson(products);
    }


}
