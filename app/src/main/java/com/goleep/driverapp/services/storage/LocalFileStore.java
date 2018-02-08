package com.goleep.driverapp.services.storage;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kunalsingh on 12/04/17.
 */

public class LocalFileStore {
    private final String APP_SHARED_PREFS = "xamcheck_parent";

    LocalFileStore(){}

    private EncryptedSharedPreferences getSharedPreference(Context context){
        return new EncryptedSharedPreferences(
                context, context.getSharedPreferences(APP_SHARED_PREFS, MODE_PRIVATE));
    }

    public void store(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void store(Context context, String key, long value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void store(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getString(Context context, String appKey) {
        String key = getSharedPreference(context).getString(appKey, null);
        if(key != null && key.equals("")){
            return null;
        }else{
            return key;
        }
    }

    public long getLong(Context context, String appKey) {
        return getSharedPreference(context).getLong(appKey, 0);
    }

    public boolean getBoolean(Context context, String appKey) {
        return getSharedPreference(context).getBoolean(appKey, false);
    }


}
