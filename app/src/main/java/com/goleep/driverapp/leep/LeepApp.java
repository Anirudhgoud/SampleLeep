package com.goleep.driverapp.leep;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.stetho.Stetho;

/**
 * Created by vishalm on 14/02/18.
 */

public class LeepApp extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Stetho.initializeWithDefaults(this);
    }
}
