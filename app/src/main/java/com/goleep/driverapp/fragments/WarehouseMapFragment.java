package com.goleep.driverapp.fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uihelpers.LocationHelper;
import com.goleep.driverapp.helpers.uimodels.Distance;
import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;

import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.WarehouseViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 23/03/18.
 */

public class WarehouseMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, LocationChangeListener {
    private WarehouseViewModel warehouseViewModel;
    private GoogleMap mGoogleMap;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    @BindView(R.id.tv_customer_name)
    CustomTextView tvCustomerName;
    @BindView(R.id.tv_do_number)
    CustomTextView tvDONumber;
    @BindView(R.id.tv_store_address)
    CustomTextView tvAddress;
    @BindView(R.id.tv_date)
    CustomTextView tvDeliveryDate;
    @BindView(R.id.tv_preferred_time)
    CustomTextView tvPreferredTime;
    @BindView(R.id.tv_time_to_reach)
    CustomTextView tvTimeToReach;
    @BindView(R.id.bt_navigate)
    ImageView ivNavigate;
    @BindView(R.id.ll_map_address_layout)
    LinearLayout llMapAddressLayout;
    @BindView(R.id.ll_do_number)
    LinearLayout doNumberLayout;
    @BindView(R.id.ll_date)
    LinearLayout datelayout;
    @BindView(R.id.ll_time)
    LinearLayout timeLayout;
    private UILevelNetworkCallback deliveryOrdersUILevelCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {

        }
    };

    private UILevelNetworkCallback timeToReachCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            getActivity().runOnUiThread(() -> {
                List<Distance> timeToReachList = (List<Distance>) uiModels;
                List<WarehouseEntity> warehouseEntities = warehouseViewModel.getWarehouses();
                if (warehouseEntities != null && warehouseEntities.size() == timeToReachList.size()) {
                    for (int i = 0; i < warehouseEntities.size() && i < timeToReachList.size(); i++) {
                        WarehouseEntity warehouseEntity = warehouseEntities.get(i);
                        Distance distance = timeToReachList.get(i);
                        warehouseEntity.setDistanceFromCurrentLocation(distance);
                    }
                    displayMarkersOnMap((warehouseEntities));
                    llMapAddressLayout.setVisibility(View.VISIBLE);
                } else {
                    llMapAddressLayout.setVisibility(View.GONE);
                }
            });
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        initialize();
        hideUnwantedViews();
        initialiseMapView();
        return view;
    }

    private void hideUnwantedViews() {
        doNumberLayout.setVisibility(View.GONE);
        datelayout.setVisibility(View.GONE);
        timeLayout.setVisibility(View.GONE);
    }

    private void initialize() {
        warehouseViewModel = ViewModelProviders.of(getActivity()).get(WarehouseViewModel.class);
        ivNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDirectionsOnGoogleMaps();
            }
        });
    }

    private void initialiseMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(this);
        checkForLocationPermission();
    }

    private void checkForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
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

    private void displayMarkersOnMap(List<WarehouseEntity> warehouseEntities) {
        mGoogleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < warehouseEntities.size(); i++) {
            WarehouseEntity warehouseEntity = warehouseEntities.get(i);
            MarkerOptions markerOptions = warehouseViewModel.getMarkerOption(warehouseEntity);
            if (markerOptions != null) {
                Marker marker = mGoogleMap.addMarker(markerOptions);
                marker.setTag(warehouseEntity);
                builder.include(marker.getPosition());
                if (i == 0) {
                    onMarkerClick(marker);
                }
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
    public boolean onMarkerClick(Marker marker) {
        warehouseViewModel.setSelectedMarker(marker);
        WarehouseEntity warehouseEntity = (WarehouseEntity) marker.getTag();
        if (warehouseEntity != null) {
            tvCustomerName.setText(warehouseEntity.getWareHouseName() == null ? "" : warehouseEntity.getWareHouseName());
            tvAddress.setText(StringUtils.getAddress(warehouseEntity.getAddressLine1(),
                    warehouseEntity.getAddressLine2()));
            tvTimeToReach.setText(warehouseEntity.getDistanceFromCurrentLocation().getDistanceText());
            updateMarkers(marker, warehouseEntity);
            warehouseViewModel.setPreviouslySelectedMarker(warehouseViewModel.getSelectedMarker());
        }
        return true;
    }

    private void updateMarkers(Marker marker, WarehouseEntity warehouseEntity) {
        Marker previousMarker = warehouseViewModel.getPreviouslySelectedMarker();
        if (previousMarker != null && previousMarker != warehouseViewModel.getSelectedMarker()) {
            WarehouseEntity prevMarkerDO = (WarehouseEntity) previousMarker.getTag();
            if (prevMarkerDO != null) {
                previousMarker.setIcon(BitmapDescriptorFactory.fromBitmap(warehouseViewModel.getMarkerBitmapFromView(prevMarkerDO.getDistanceFromCurrentLocation().getDurationText(), false)));
            }
        }
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(warehouseViewModel.getMarkerBitmapFromView(warehouseEntity.getDistanceFromCurrentLocation().getDurationText(), true)));
    }

    @Override
    public void onLastKnownLocationReceived(Location location) {
        if (location != null)
        warehouseViewModel.fetchTimeToReachAndUpdateDeliveryOrders(
                warehouseViewModel.getWarehouses(), location, timeToReachCallback);
        else {
            Toast.makeText(getContext(), getContext().getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLastKnownLocationError(String errorMessage) {
        Toast.makeText(getContext(), getContext().getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
    }

    private void openDirectionsOnGoogleMaps() {
        Marker selectedMarker = warehouseViewModel.getSelectedMarker();
        if (selectedMarker != null) {
            LatLng destinationLatLng = selectedMarker.getPosition();
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(destinationLatLng.latitude) + "," +
                    String.valueOf(destinationLatLng.longitude));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }
}
