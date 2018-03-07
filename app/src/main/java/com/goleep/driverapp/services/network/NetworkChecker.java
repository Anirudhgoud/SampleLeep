package com.goleep.driverapp.services.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.goleep.driverapp.R;


/**
 * Created by kunalsingh on 12/04/17.
 */

public class NetworkChecker {

    public NetworkChecker(){}

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getNetworkErrorMessage(Context context){
        return context.getResources().getString(R.string.network_error_message);
    }
}
