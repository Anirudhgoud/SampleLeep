package com.goleep.driverapp.helpers.uihelpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by anurag on 21/03/18.
 */
@SuppressLint("MissingPermission")
public class LocationHelper implements OnSuccessListener<Location>, OnFailureListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationChangeListener locationChangeListener;

    private LocationCallback locationCallback = new LocationCallback(){

        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null || locationChangeListener == null) return;
            for (Location location : locationResult.getLocations()) {
                locationChangeListener.onLocationUpdateReceived(location);
            }
        }
    };

    public LocationHelper(Context context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public LocationHelper(Context context, long locationUpdateInterval) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        createLocationRequest(locationUpdateInterval);
    }

    private void createLocationRequest(long locationUpdateInterval){
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(locationUpdateInterval);
    }

    public void setLocationChangeListener(LocationChangeListener locationChangeListener) {
        this.locationChangeListener = locationChangeListener;
    }


    public void getLastKnownLocation(Activity context) {
        Task<Location> locationTask = mFusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(context, this);
        locationTask.addOnFailureListener(context, this);
    }

    public void startLocationUpdates(){
        if (locationRequest == null) return;
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public void stopLocationUpdates(){
        if (mFusedLocationClient != null && locationCallback != null){
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onSuccess(Location location) {
        if (locationChangeListener != null) locationChangeListener.onLastKnownLocationReceived(location);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (locationChangeListener != null) locationChangeListener.onLastKnownLocationError(e.getMessage());
    }
}
