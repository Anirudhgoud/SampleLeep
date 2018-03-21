package com.goleep.driverapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    public static File fileFromBitmap(Context context, Bitmap bitmap, String fileName) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, fileName + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(AppUtils.class.getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
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

    public static Typeface getTypeface(Context context, String fontName){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + fontName + ".ttf");
        if(typeface == null){
            typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + "NotoSans-Regular" + ".ttf");
        }
        return typeface;
    }
}
