package com.goleep.driverapp.services.room;

import android.arch.persistence.room.TypeConverter;

import com.goleep.driverapp.services.room.entities.ProductEntity;
import com.goleep.driverapp.services.room.entities.UserEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by vishalm on 21/02/18.
 */

public class Converters {
    @TypeConverter
    public static List<UserEntity.Location> fromString(String value) {
        Type listType = new TypeToken<List<UserEntity.Location>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<UserEntity.Location> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static UserEntity.Driver driverFromString(String value){
        Type type = new TypeToken<UserEntity.Driver>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String stringFromDriver(UserEntity.Driver driver){
        return new Gson().toJson(driver);
    }

    @TypeConverter
    public static UserEntity.Permissions permissionsFromString(String value){
        Type type = new TypeToken<UserEntity.Permissions>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String stringFromPermissions(UserEntity.Permissions permissions){
        return new Gson().toJson(permissions);
    }

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
