package com.goleep.driverapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

    public static void toggleKeyboard(View view, Context context){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null){
            inputMethodManager.toggleSoftInputFromWindow(
                    view.getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        }
    }
}
