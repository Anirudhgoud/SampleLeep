package com.goleep.driverapp.leep;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by vishalm on 14/02/18.
 */

public class LeepApp extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
