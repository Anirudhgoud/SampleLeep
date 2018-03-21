package com.goleep.driverapp.viewmodels;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 14/02/18.
 */

public class DropOffDeliveryOrdersViewModel extends DeliveryOrderViewModel {

    private LocationManager locationManager;

    public DropOffDeliveryOrdersViewModel(@NonNull Application application) {
        super(application);
        locationManager = (LocationManager) application.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
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
                                if (deliveryOrders.size() == 0) {
                                    timeToReachCallback.onResponseReceived(null, false, null, false);
                                }
                                for (int i = 0; i < deliveryOrders.size() && i < timeToReachList.size(); i++) {
                                    DeliveryOrderEntity deliveryOrder = deliveryOrders.get(i);
                                    Distance distance = timeToReachList.get(i);
                                    deliveryOrder.setDistanceFromCurrentLocation(distance);
                                }
                                timeToReachCallback.onResponseReceived(deliveryOrders, false,
                                        null, false);
                                break;

                            default:
                                timeToReachCallback.onResponseReceived(null, false, null, false);
                                break;
                        }
                    }
                });
    }

    public List<MarkerOptions> getMarkerOptions(List<DeliveryOrderEntity> deliveryOrders) {
        List<MarkerOptions> markerOptions = new ArrayList<>();
        for (DeliveryOrderEntity deliveryOrder : deliveryOrders) {
            markerOptions.add(getMarkerOption(deliveryOrder));
        }
        return markerOptions;
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


    private LatLng getLatLng(double latitude, double longitude) {
        return (latitude != 0 && longitude != 0) ? new LatLng(latitude, longitude) : null;
    }

    private Bitmap getMarkerBitmapFromView(String timeToReach) {
//        View customMarkerView = ((LayoutInflater) getApplication().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
//                inflate(R.layout.map_marker_title_layout, null);
//        ((CustomTextView) customMarkerView.findViewById(R.id.time_to_reach_tv)).setText(timeToReach);
//        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
//        customMarkerView.buildDrawingCache();
//        return AppUtils.bitmapFromView(customMarkerView, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());

        View customMarkerView = ((LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.map_marker_title_layout, null);
        ((CustomTextView) customMarkerView.findViewById(R.id.time_to_reach_tv)).setText(timeToReach);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
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

}
