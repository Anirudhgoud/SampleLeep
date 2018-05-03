package com.tracotech.tracoman.utils;

import android.util.Log;

import com.tracotech.tracoman.BuildConfig;


/**
 * Created by kunalsingh on 10/04/17.
 */

public class LogUtils {

    public static void info(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void debug(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void error(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }
}
