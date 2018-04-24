package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.view.View;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.helpers.customviews.CustomMarkerView;
import com.goleep.driverapp.helpers.uimodels.Distance;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.DistanceMatrixResponseParser;
import com.goleep.driverapp.services.room.AppDatabase;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.MapUtils;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 29/03/18.
 */

public class WarehouseViewModel extends AndroidViewModel {

    private AppDatabase leepDatabase;
    private Marker previouslySelectedMarker;
    private Marker selectedMarker;

    public WarehouseViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

   public List<WarehouseEntity> getWarehouses() {
        return leepDatabase.warehouseDao().getAllWarehouses();
    }

    public Marker getPreviouslySelectedMarker() {
        return previouslySelectedMarker;
    }

    public void setPreviouslySelectedMarker(Marker previouslySelectedMarker) {
        this.previouslySelectedMarker = previouslySelectedMarker;
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        this.selectedMarker = selectedMarker;
    }

    public void fetchTimeToReachAndUpdateDeliveryOrders(List<WarehouseEntity> warehouseEntities,
                                                        Location currentLocation,
                                                        UILevelNetworkCallback timeToReachCallback) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication().getApplicationContext(),
                MapUtils.generateDistanceMatrixUrl(getOrigins(currentLocation),
                        getDestinations(warehouseEntities), getApplication()),
                false, (type, response, errorMessage) -> {
                    switch (type) {
                        case NetworkConstants.SUCCESS:
                            List<Distance> timeToReachList = new DistanceMatrixResponseParser().
                                    parseDistanceMatrixResponse(response.optJSONObject(0));
                            timeToReachCallback.onResponseReceived(timeToReachList, false,
                                    null, false);
                            break;

                        default:
                            timeToReachCallback.onResponseReceived(null,
                                    false, null, false);
                            break;
                    }
                });
    }

    private List<LatLng> getDestinations(List<WarehouseEntity> warehouseEntities) {
        List<LatLng> destinations = new ArrayList<>();
        for (WarehouseEntity warehouseEntity : warehouseEntities) {
            LatLng destination = getLatLng(warehouseEntity.getLatitude(), warehouseEntity.getLongitude());
            destinations.add(destination);
        }
        return destinations;
    }

    public LatLng getLatLng(double latitude, double longitude) {
        return (latitude != 0 && longitude != 0) ? new LatLng(latitude, longitude) : null;
    }

    private List<LatLng> getOrigins(Location location) {
        List<LatLng> origins = new ArrayList<>();
        origins.add(new LatLng(location.getLatitude(), location.getLongitude()));
        return origins;
    }

    public MarkerOptions getMarkerOption(WarehouseEntity warehouseEntity) {
        MarkerOptions markerOptions = null;
        LatLng destinationLatLng = getLatLng(warehouseEntity.getLatitude(), warehouseEntity.getLongitude());
        if (destinationLatLng != null) {
            markerOptions = new MarkerOptions().position(destinationLatLng);
            Distance distance = warehouseEntity.getDistanceFromCurrentLocation();
            if (distance != null) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(distance.getDurationText(), false)));
            }
        }
        return markerOptions;
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

}
