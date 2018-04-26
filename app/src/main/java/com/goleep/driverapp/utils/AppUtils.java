package com.goleep.driverapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.helpers.uimodels.Country;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by anurag on 19/02/18.
 */

public class AppUtils {

    public static String userCurrencySymbol(Context context) {
        Country selectedCountry = new Gson().fromJson(LocalStorageService.sharedInstance().
                getLocalFileStore().getString(context, SharedPreferenceKeys.SELECTED_COUNTRY), Country.class);
        return selectedCountry.getCurrencySymbol();
    }

    public static Bitmap bitmapFromView(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(canvas);
        return bitmap;
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

    public static void toggleKeyboard(View view) {
        InputMethodManager inputMethodManager = inputMethodManager(view);
        if (inputMethodManager == null) return;
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public static void showKeyboard(View view) {
        InputMethodManager inputMethodManager = inputMethodManager(view);
        if (inputMethodManager == null) return;
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = inputMethodManager(view);
        if (inputMethodManager == null) return;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private static InputMethodManager inputMethodManager(View view){
        if (view == null) return null;
        return  (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static boolean isValidEmail(String strEmail) {
        if (strEmail.length() == 0)
            return false;
        else {
            final String PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            return strEmail.matches(PATTERN);
        }
    }
}
