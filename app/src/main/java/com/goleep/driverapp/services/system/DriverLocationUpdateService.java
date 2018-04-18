package com.goleep.driverapp.services.system;

import android.location.Location;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uihelpers.LocationHelper;
import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.utils.LogUtils;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class DriverLocationUpdateService extends JobService implements LocationChangeListener {

    private final long LOCATION_INTERVAL_IN_MILLIS = 20000;
    private LocationHelper locationHelper;

    @Override
    public boolean onStartJob(JobParameters job) {
        LogUtils.error("", "onStartJob");
        locationHelper = new LocationHelper(this, LOCATION_INTERVAL_IN_MILLIS);
        locationHelper.setLocationChangeListener(this);
        locationHelper.startLocationUpdates();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (locationHelper != null) locationHelper.stopLocationUpdates();
        LogUtils.error("", "onStopJob");
        return false;
    }

    @Override
    public void onLastKnownLocationReceived(Location location) {
    }

    @Override
    public void onLastKnownLocationError(String errorMessage) {
        LogUtils.error("", errorMessage);
    }

    @Override
    public void onLocationUpdateReceived(Location location) {
        if (location != null) {
            LogUtils.error("", location.toString());
            updateCurrentLocation(location);
        }
    }

    private void updateCurrentLocation(Location location) {

        NetworkService.sharedInstance().getNetworkClient().makeJsonPostRequest(this, UrlConstants.DRIVER_CURRENT_LOCATION_URL, true, generateLocationRequestMap(location), new NetworkAPICallback() {
            @Override
            public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                LogUtils.error("DriverLocationUpdateService", response == null ? "Error while updating location" : response.toString());
            }
        });

    }

    private Map<String, Object> generateLocationRequestMap(Location location) {
        Map<String, Object> httpBody = new HashMap<>();
        httpBody.put("latitude", location.getLatitude());
        httpBody.put("longitude", location.getLongitude());
        return httpBody;
    }

}
