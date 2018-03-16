package com.goleep.driverapp.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Distance;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.DistanceMatrixResponseParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 12/03/18.
 */

public class PickupMapViewModel extends AndroidViewModel {
    private AppDatabase leepDatabase;
    private LocationManager locationManager;
    private LatLng userCurrentLatLng;

    public PickupMapViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
        locationManager = (LocationManager) application.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    public String getWareHouseNameAddress() {
        DriverEntity driverEntity = RoomDBService.sharedInstance().getDatabase(
                this.getApplication().getApplicationContext()).driverDao().getDriver();
        return driverEntity.getLocationName() + ", " + driverEntity.getAddressLine1() + ", " + driverEntity.getAddressLine2();
    }

    public String getWareHouseName() {
        DriverEntity driverEntity = RoomDBService.sharedInstance().getDatabase(
                this.getApplication().getApplicationContext()).driverDao().getDriver();
        return driverEntity.getLocationName();
    }

    public LatLng getWarehouseLatLng() {
        DriverEntity driverEntity = leepDatabase.driverDao().getDriver();
        if (driverEntity.getWorkLocationLat() != 0.0 || driverEntity.getWorkLocationLng() != 0.0)
            return new LatLng(driverEntity.getWorkLocationLat(),
                    driverEntity.getWorkLocationLng());
        return null;
    }

    public String getWarehouseAddress() {
        DriverEntity driverEntity = RoomDBService.sharedInstance().getDatabase(
                this.getApplication().getApplicationContext()).driverDao().getDriver();
        return driverEntity.getAddressLine1() + ", " + driverEntity.getAddressLine2() + ", " +
                driverEntity.getCity() + ", " + driverEntity.getState() + " - " + driverEntity.getPinCode();
    }

    public void getTimeToReach(LatLng source, LatLng destination, UILevelNetworkCallback timeToReachCallback){
        String url = UrlConstants.DISTANCE_MATRIX_API+"origins="+
                String.valueOf(source.latitude)+","+String.valueOf(source.longitude)+"&destinations="+
                String.valueOf(destination.latitude)+","+String.valueOf(destination.longitude)+"&key="+
                getApplication().getApplicationContext().getResources().getString(R.string.google_maps_key);
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(), url,
                false, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        System.out.print("");
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                List<String> result = new ArrayList<>();
                                List<Distance> timeToReachList = new DistanceMatrixResponseParser().
                                        parseDistanceMatrixResponse(response.optJSONObject(0));
                                if(timeToReachList.size() >0)
                                    result.add(timeToReachList.get(0).getDurationText());
                                timeToReachCallback.onResponseReceived(result, false, null, false);
                                break;
                        }
                    }
                });
    }

    @SuppressLint("MissingPermission")
    public LatLng getUserLocation() {
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if(location != null){
            userCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        }
        return userCurrentLatLng;
    }

    public LatLng getUserCurrentLatLng() {
        return userCurrentLatLng;
    }
}
