package com.tracotech.tracoman.services.room;

import android.arch.persistence.room.Room;

import android.content.Context;



/**
 * Created by vishalm on 09/02/18.
 */

public class RoomDBService {
    private static RoomDBService roomDBService;
    private static AppDatabase roomDB;
    private RoomDBService(){

    }

    public static synchronized RoomDBService sharedInstance(){
        if(roomDBService == null)
            roomDBService = new RoomDBService();
        return roomDBService;
    }

    public AppDatabase getDatabase(Context context){
        return roomDB != null? roomDB: Room.databaseBuilder(context, AppDatabase.class,
                "leep_db").allowMainThreadQueries().build();

    }
}
