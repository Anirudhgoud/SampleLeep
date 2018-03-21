package com.goleep.driverapp.fragments;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uihelpers.LocationHelper;
import com.goleep.driverapp.helpers.uimodels.Distance;
import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.viewmodels.DropOffDeliveryOrdersViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by anurag on 15/02/18.
 */

public class DeliveryOrdersMapFragment extends Fragment implements OnMapReadyCallback, LocationChangeListener {

    private DropOffDeliveryOrdersViewModel viewModel;
    private GoogleMap mGoogleMap;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_order_map, container, false);
        initialiseMapView();
        return view;
    }

    private void initialiseMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        checkForLocationPermission();
    }

    private void checkForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        onPermissionGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted();
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void onPermissionGranted() {
        fetchUserLocation();
    }

    private void fetchUserLocation() {
        LocationHelper locationHelper = new LocationHelper(getActivity());
        locationHelper.setLocationChangeListener(this);
        locationHelper.getLastKnownLocation(getActivity());
    }

    private void observeDeliveryOrders(Location location) {
        viewModel = ViewModelProviders.of(getActivity()).get(DropOffDeliveryOrdersViewModel.class);
        viewModel.getDeliveryOrders(DropOffDeliveryOrdersViewModel.TYPE_CUSTOMER,
                DropOffDeliveryOrdersViewModel.STATUS_IN_TRANSIT).observe(
                DeliveryOrdersMapFragment.this, new Observer<List<DeliveryOrderEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<DeliveryOrderEntity> deliveryOrders) {
                        viewModel.fetchTimeToReachAndUpdateDeliveryOrders(deliveryOrders, location, timeToReachCallback);
                    }
                });
    }

    private void displayLocationsOnMap(List<DeliveryOrderEntity> deliveryOrders) {
        mGoogleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (DeliveryOrderEntity deliveryOrder : deliveryOrders) {
            MarkerOptions markerOptions = viewModel.getMarkerOption(deliveryOrder);
            if (markerOptions != null) {
                Marker marker = mGoogleMap.addMarker(markerOptions);
                marker.setTag(deliveryOrder);
                builder.include(marker.getPosition());
            }
        }

        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (height * 0.10);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onLastKnownLocationReceived(Location location) {
        if (location == null) {
            Toast.makeText(getContext(), getContext().getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
        } else {
            observeDeliveryOrders(location);
        }
    }

    @Override
    public void onLastKnownLocationError(String errorMessage) {
        Toast.makeText(getContext(), getContext().getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
    }

    private UILevelNetworkCallback timeToReachCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            getActivity().runOnUiThread(() -> {
                List<Distance> timeToReachList = (List<Distance>) uiModels;
                viewModel.setTimeToReachDistanceMatrix(timeToReachList);

                List<DeliveryOrderEntity> deliveryOrders = viewModel.getDeliveryOrders();
                if (deliveryOrders.size() == timeToReachList.size()) {
                    for (int i = 0; i < deliveryOrders.size() && i < timeToReachList.size(); i++) {
                        DeliveryOrderEntity deliveryOrder = deliveryOrders.get(i);
                        Distance distance = timeToReachList.get(i);
                        deliveryOrder.setDistanceFromCurrentLocation(distance);
                    }
                    displayLocationsOnMap((deliveryOrders));
                }

            });
        }
    };
}
