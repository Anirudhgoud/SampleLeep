package com.goleep.driverapp.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by vishalm on 21/03/18.
 */

public class FontUtils {

    public Typeface getTypeface(Context context, String fontName){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + fontName + ".ttf");
        if(typeface == null){
            typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + "NotoSans-Regular" + ".ttf");
        }
        return typeface;
    }
}
