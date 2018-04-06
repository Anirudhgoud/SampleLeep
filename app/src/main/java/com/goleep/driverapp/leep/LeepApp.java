package com.goleep.driverapp.leep;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.stetho.Stetho;
import com.goleep.driverapp.helpers.uihelpers.FontProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vishalm on 14/02/18.
 */

public class LeepApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Stetho.initializeWithDefaults(this);
        FontProvider.init(this);
    }
}
