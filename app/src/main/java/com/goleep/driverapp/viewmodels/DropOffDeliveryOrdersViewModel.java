package com.goleep.driverapp.viewmodels;


import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.View;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.customviews.CustomMarkerView;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.helpers.uimodels.Distance;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.DistanceMatrixResponseParser;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.MapUtils;
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

    private Marker previouslySelectedMarker;
    private Marker selectedMarker;
    private MutableLiveData<List<Distance>> timeToReachDistanceMatrix = new MutableLiveData<>();

    public DropOffDeliveryOrdersViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchTimeToReachAndUpdateDeliveryOrders(List<DeliveryOrderEntity> deliveryOrders,
                                                        Location currentLocation,
                                                        UILevelNetworkCallback timeToReachCallback) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                MapUtils.generateDistanceMatrixUrl(getOrigins(currentLocation),
                        getDestinations(deliveryOrders), getApplication()),
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
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(distance.getDurationText(), false)));
            }
        }
        return markerOptions;
    }


    public LatLng getLatLng(double latitude, double longitude) {
        return (latitude != 0 && longitude != 0) ? new LatLng(latitude, longitude) : null;
    }

    public Bitmap getMarkerBitmapFromView(String timeToReach, boolean isSelected) {
        View customMarkerView = new CustomMarkerView(getApplication().getApplicationContext(), timeToReach, isSelected);
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

    public String dateToDisplay(String dateString) {
        return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString,
                DateTimeUtils.ORDER_SERVER_DATE_FORMAT, DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA);

    }

    public String timeToDisplay(String timeString) {
        if (timeString != null) {
            String[] times = timeString.split(" - ");
            if (times.length == 2) {
                String startTime = DateTimeUtils.convertdDate(times[0].trim(),
                        DateTimeUtils.TWENTY_FOUR_HOUR_TIME_FORMAT, DateTimeUtils.TWELVE_HOUR_TIME_FORMAT);
                String endTime = DateTimeUtils.convertdDate(times[1].trim(),
                        DateTimeUtils.TWENTY_FOUR_HOUR_TIME_FORMAT, DateTimeUtils.TWELVE_HOUR_TIME_FORMAT);
                return startTime + " - " + endTime;
            }
        }
        return "-";
    }

    public String getAddress(String line1, String line2) {
        String addressLine1 = line1 == null ? "" : line1;
        String addressLine2 = line2 == null ? "" : line2;
        String separator = line1 == null ? "" : ", ";
        return addressLine1 + separator + addressLine2;
    }

    public String getEstimatedDeliveryTimeText(Distance distance) {
        if (distance == null) return "";
        return distance.getDurationText() == null ? "" : distance.getDurationText();

    }

    public List<BaseListItem> updatedOrders(List<DeliveryOrderEntity> deliveryOrders, List<Distance> distances) {
        int deliveryOrderSize = deliveryOrders.size();
        int distanceListSize = distances.size();

        if (deliveryOrderSize == distanceListSize) {
            for (int i = 0; i < deliveryOrderSize; i++) {
                DeliveryOrderEntity deliveryOrder = deliveryOrders.get(i);
                Distance distance = distances.get(i);
                deliveryOrder.setDistanceFromCurrentLocation(distance);
            }
            List<BaseListItem> baseListItems = new ArrayList<>();
            baseListItems.addAll(deliveryOrders);
            return baseListItems;
        }
        return null;
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

    public Marker getPreviouslySelectedMarker() {
        return previouslySelectedMarker;
    }

    public void setPreviouslySelectedMarker(Marker previouslySelectedMarker) {
        this.previouslySelectedMarker = previouslySelectedMarker;
    }
}
