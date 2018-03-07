package com.goleep.driverapp.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by anurag on 19/02/18.
 */

public class AppUtils {

    public static String userCurrencySymbol(){
        //TODO: Get appropriate currency symbol according to user country or fetch from APIs
        return "₹";
    }

    public static Bitmap bitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width , height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }
}
