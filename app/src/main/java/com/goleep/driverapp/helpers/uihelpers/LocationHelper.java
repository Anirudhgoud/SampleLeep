package com.goleep.driverapp.helpers.uihelpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by anurag on 21/03/18.
 */

public class LocationHelper implements OnSuccessListener<Location>, OnFailureListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationChangeListener locationChangeListener;

    public LocationHelper(Context context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void setLocationChangeListener(LocationChangeListener locationChangeListener) {
        this.locationChangeListener = locationChangeListener;
    }

    @SuppressLint("MissingPermission")
    public void getLastKnownLocation(Activity context) {
        Task<Location> locationTask = mFusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(context, this);
        locationTask.addOnFailureListener(context, this);
    }

    @Override
    public void onSuccess(Location location) {
        if (locationChangeListener != null) {
            locationChangeListener.onLastKnownLocationReceived(location);
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (locationChangeListener != null) {
            locationChangeListener.onLastKnownLocationError(e.getMessage());
        }
    }
}
