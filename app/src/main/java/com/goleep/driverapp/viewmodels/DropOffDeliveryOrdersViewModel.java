package com.goleep.driverapp.viewmodels;


import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.Distance;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.DistanceMatrixResponseParser;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 14/02/18.
 */

public class DropOffDeliveryOrdersViewModel extends DeliveryOrderViewModel {

    private Marker selectedMarker;
    private MutableLiveData<List<Distance>> timeToReachDistanceMatrix = new MutableLiveData<>();

    public DropOffDeliveryOrdersViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchTimeToReachAndUpdateDeliveryOrders(List<DeliveryOrderEntity> deliveryOrders, Location currentLocation, UILevelNetworkCallback timeToReachCallback) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(), generateDistanceMatrixUrl(getOrigins(currentLocation), getDestinations(deliveryOrders)),
                false, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type) {
                            case NetworkConstants.SUCCESS:
                                List<Distance> timeToReachList = new DistanceMatrixResponseParser().
                                        parseDistanceMatrixResponse(response.optJSONObject(0));
                                timeToReachCallback.onResponseReceived(timeToReachList, false,
                                        null, false);
                                break;

                            default:
                                timeToReachCallback.onResponseReceived(null, false, null, false);
                                break;
                        }
                    }
                });
    }

    public MarkerOptions getMarkerOption(DeliveryOrderEntity deliveryOrder) {
        MarkerOptions markerOptions = null;
        LatLng destinationLatLng = getLatLng(deliveryOrder.getDestinationLatitude(), deliveryOrder.getDestinationLongitude());
        if (destinationLatLng != null) {
            markerOptions = new MarkerOptions().position(destinationLatLng);
            Distance distance = deliveryOrder.getDistanceFromCurrentLocation();
            if (distance != null) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(distance.getDurationText())));
            }
        }
        return markerOptions;
    }


    public LatLng getLatLng(double latitude, double longitude) {
        return (latitude != 0 && longitude != 0) ? new LatLng(latitude, longitude) : null;
    }

    private Bitmap getMarkerBitmapFromView(String timeToReach) {
        View customMarkerView = ((LayoutInflater) getApplication().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.map_marker_title_layout, null);
        ((CustomTextView) customMarkerView.findViewById(R.id.time_to_reach_tv)).setText(timeToReach);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.setDrawingCacheEnabled(true);
        customMarkerView.invalidate();
        customMarkerView.buildDrawingCache(false);
        return AppUtils.bitmapFromView(customMarkerView, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
    }

    private List<LatLng> getDestinations(List<DeliveryOrderEntity> deliveryOrders) {
        List<LatLng> destinations = new ArrayList<>();
        for (DeliveryOrderEntity deliveryOrder : deliveryOrders) {
            LatLng destination = getLatLng(deliveryOrder.getDestinationLatitude(), deliveryOrder.getDestinationLongitude());
            destinations.add(destination);
        }
        return destinations;
    }

    private List<LatLng> getOrigins(Location location) {
        List<LatLng> origins = new ArrayList<>();
        origins.add(new LatLng(location.getLatitude(), location.getLongitude()));
        return origins;
    }


    private String generateDistanceMatrixUrl(List<LatLng> origins, List<LatLng> destinations) {
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
        url.append(getApplication().getApplicationContext().getResources().getString(R.string.google_maps_key));
        return url.toString();
    }

    public String dateToDisplay(String dateString) {
        return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString, "yyyy-MM-dd", "dd MMM, yyyy");

    }

    public String timeToDisplay(String timeString) {
        if (timeString != null) {
            String[] times = timeString.split(" - ");
            if (times.length == 2) {
                String startTime = DateTimeUtils.convertdDate(times[0].trim(), "HH:mm", "hh:mma");
                String endTime = DateTimeUtils.convertdDate(times[1].trim(), "HH:mm", "hh:mma");
                return startTime + " - " + endTime;
            }
        }
        return "-";
    }

    public String getAddress(String line1, String line2) {
        String address = "";
        if (line1 != null) {
            address = line1;
        }
        if (line2 != null) {
            if (line1 != null) {
                address += ", ";
            }
            address = address + line2;
        }
        return address;
    }

    public String getEstimatedDeliveryTimeText(Distance distance) {
        if (distance == null) return "";
        else {
            return distance.getDurationText() == null ? "" : distance.getDurationText();
        }
    }

    //Getters and setters
    public void setTimeToReachDistanceMatrix(List<Distance> timeToReachDistanceMatrix) {
        this.timeToReachDistanceMatrix.setValue(timeToReachDistanceMatrix);
    }

    public MutableLiveData<List<Distance>> getTimeToReachDistanceMatrix() {
        return timeToReachDistanceMatrix;
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        this.selectedMarker = selectedMarker;
    }
}
