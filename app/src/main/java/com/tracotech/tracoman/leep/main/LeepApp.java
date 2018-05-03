package com.tracotech.tracoman.leep.main;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by vishalm on 14/02/18.
 */

public class LeepApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
