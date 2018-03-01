package com.goleep.driverapp.services.room;

import android.arch.persistence.room.TypeConverter;

import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.DoDetails;
import com.goleep.driverapp.services.room.entities.Product;
import com.goleep.driverapp.services.room.entities.UserMeta;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by vishalm on 21/02/18.
 */

public class Converters {
    @TypeConverter
    public static List<UserMeta.Location> fromString(String value) {
        Type listType = new TypeToken<List<UserMeta.Location>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<UserMeta.Location> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static UserMeta.Driver driverFromString(String value){
        Type type = new TypeToken<UserMeta.Driver>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String stringFromDriver(UserMeta.Driver driver){
        return new Gson().toJson(driver);
    }

    @TypeConverter
    public static UserMeta.Permissions permissionsFromString(String value){
        Type type = new TypeToken<UserMeta.Permissions>(){}.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String stringFromPermissions(UserMeta.Permissions permissions){
        return new Gson().toJson(permissions);
    }

    @TypeConverter
    public static List<DeliveryOrderItem> doItemFromString(String value){
        Type type = new TypeToken<List<DeliveryOrderItem>>(){}.getType();
        return new Gson().fromJson(value, type);
    }
    @TypeConverter
    public static String stringFromDoItem(List<DeliveryOrderItem> doItem){
        return new Gson().toJson(doItem);
    }


}
