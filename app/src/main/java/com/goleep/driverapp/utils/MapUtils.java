package com.goleep.driverapp.utils;

import android.content.Context;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.UrlConstants;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by vishalm on 29/03/18.
 */

public class MapUtils {

    public static String generateDistanceMatrixUrl(List<LatLng> origins, List<LatLng> destinations, Context context) {
        StringBuilder url = new StringBuilder();
        url.append(UrlConstants.DISTANCE_MATRIX_API);

        if (origins != null && origins.size() > 0) {
            url.append("origins=");
            for (int i = 0; i < origins.size(); i++) {
                LatLng origin = origins.get(i);
                if (i != 0) url.append("|");
                url.append(origin.latitude);
                url.append(",");
                url.append(origin.longitude);
            }
        }
        if (destinations != null && destinations.size() > 0) {
            url.append("&destinations=");
            for (int i = 0; i < destinations.size(); i++) {
                LatLng destination = destinations.get(i);
                if (i != 0) url.append("|");
                url.append(destination.latitude);
                url.append(",");
                url.append(destination.longitude);
            }
        }
        url.append("&key=");
        url.append(context.getResources().getString(R.string.google_maps_key));
        return url.toString();
    }

}
