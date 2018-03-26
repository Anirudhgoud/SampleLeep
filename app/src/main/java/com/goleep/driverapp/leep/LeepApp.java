package com.goleep.driverapp.leep;

import android.app.Application;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.stetho.Stetho;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vishalm on 14/02/18.
 */

public class LeepApp extends Application {
    private Map<String, Typeface> typefaceMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Stetho.initializeWithDefaults(this);
        initTypefaces();
    }

    public Typeface getTypeface(String fontName) {
        Typeface typeface = typefaceMap.get(fontName);
        if(typeface == null){
            typeface = Typeface.createFromAsset(getAssets(),
                    "fonts/" + "NotoSans-Regular" + ".ttf");
        }
        return typeface;
    }

    private void initTypefaces() {
        Typeface typeface = Typeface.createFromAsset(getAssets(),
                "fonts/" + "NotoSans-Bold" + ".ttf");
        typefaceMap.put("NotoSans-Bold", typeface);
        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/" + "NotoSans-Light" + ".ttf");
        typefaceMap.put("NotoSans-Light", typeface);
        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/" + "NotoSans-Medium" + ".ttf");
        typefaceMap.put("NotoSans-Medium", typeface);
        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/" + "NotoSans-Regular" + ".ttf");
        typefaceMap.put("NotoSans-Regular", typeface);
        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/" + "NotoSans-Thin" + ".ttf");
        typefaceMap.put("NotoSans-Thin", typeface);
    }
}
