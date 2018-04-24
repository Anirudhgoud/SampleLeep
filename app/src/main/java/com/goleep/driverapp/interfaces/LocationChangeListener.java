package com.goleep.driverapp.interfaces;

import android.location.Location;

/**
 * Created by anurag on 21/03/18.
 */

public interface LocationChangeListener {
    void onLastKnownLocationReceived(Location location);

    void onLastKnownLocationError(String errorMessage);

    default void onLocationUpdateReceived(Location location) {
    }
}
