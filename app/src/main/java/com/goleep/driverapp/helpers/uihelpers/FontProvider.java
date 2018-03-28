package com.goleep.driverapp.helpers.uihelpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vishalm on 28/03/18.
 */

public class FontProvider {

    private static final Map<String, Typeface> typefaceMap =  new HashMap<>();

    public static String BOLD = "NotoSans-Bold";
    public static String LIGHT = "NotoSans-Light";
    public static String MEDIUM = "NotoSans-Medium";
    public static String REGULAR = "NotoSans-Regular";
    public static String THIN = "NotoSans-Thin";

    public static void init(Context context) {
        AssetManager assetManager = context.getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager,
                "fonts/" + BOLD + ".ttf");
        typefaceMap.put(BOLD, typeface);
        typeface = Typeface.createFromAsset(assetManager,
                "fonts/" + LIGHT + ".ttf");
        typefaceMap.put(LIGHT, typeface);
        typeface = Typeface.createFromAsset(assetManager,
                "fonts/" + MEDIUM + ".ttf");
        typefaceMap.put(MEDIUM, typeface);
        typeface = Typeface.createFromAsset(assetManager,
                "fonts/" + REGULAR + ".ttf");
        typefaceMap.put(REGULAR, typeface);
        typeface = Typeface.createFromAsset(assetManager,
                "fonts/" + THIN + ".ttf");
        typefaceMap.put(THIN, typeface);
    }

    public static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = typefaceMap.get(fontName);
        if(typeface == null){
            typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FontProvider.REGULAR + ".ttf");
        }
        return typeface;
    }
}
