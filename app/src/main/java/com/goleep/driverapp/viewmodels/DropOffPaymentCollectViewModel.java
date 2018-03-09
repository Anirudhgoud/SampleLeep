package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;

/**
 * Created by anurag on 09/03/18.
 */

public class DropOffPaymentCollectViewModel extends AndroidViewModel {

    protected AppDatabase leepDatabase;

    public DropOffPaymentCollectViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public void test() {
        getApplication().getApplicationContext();
    }
}
