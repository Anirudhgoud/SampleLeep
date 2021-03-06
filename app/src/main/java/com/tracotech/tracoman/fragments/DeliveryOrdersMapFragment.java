package com.tracotech.tracoman.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.constants.Permissions;
import com.tracotech.tracoman.helpers.uihelpers.LocationHelper;
import com.tracotech.tracoman.helpers.uihelpers.PermissionHelper;
import com.tracotech.tracoman.helpers.uimodels.Distance;
import com.tracotech.tracoman.interfaces.LocationChangeListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.dropoff.deliveryorders.DropOffDeliveryOrderDetailsActivity;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.viewmodels.dropoff.deliveryorders.DropOffDeliveryOrdersViewModel;
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

import static com.tracotech.tracoman.constants.AppConstants.LOCATION_PERMISSION_REQUEST_CODE;

/**
 * Created by anurag on 15/02/18.
 */

public class DeliveryOrdersMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationChangeListener {

    private TextView tvCustomerName;
    private TextView tvDONumber;
    private TextView tvAddress;
    private TextView tvDeliveryDate;
    private TextView tvPreferredTime;
    private TextView tvTimeToReach;
    private ImageView ivNavigate;
    private LinearLayout llMapAddressLayout;

    private DropOffDeliveryOrdersViewModel viewModel;
    private GoogleMap mGoogleMap;
    private PermissionHelper permissionHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initialise();
        connectUIElements(view);
        addListeners();
        initialiseMapView();
        return view;
    }

    private void initialise(){
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        viewModel = ViewModelProviders.of(activity).get(DropOffDeliveryOrdersViewModel.class);
    }

    private void initialiseMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void connectUIElements(View view) {
        tvCustomerName = view.findViewById(R.id.tv_customer_name);
        tvDONumber = view.findViewById(R.id.tv_do_number);
        tvAddress = view.findViewById(R.id.tv_store_address);
        tvDeliveryDate = view.findViewById(R.id.tv_date);
        tvPreferredTime = view.findViewById(R.id.tv_preferred_time);
        tvTimeToReach = view.findViewById(R.id.tv_time_to_reach);
        ivNavigate = view.findViewById(R.id.bt_navigate);
        llMapAddressLayout = view.findViewById(R.id.ll_map_address_layout);
    }

    private void addListeners() {
        ivNavigate.setOnClickListener(v -> openDirectionsOnGoogleMaps());
        llMapAddressLayout.setOnClickListener(v -> onMarkerDetailsTap());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(this);
        checkForLocationPermission();
    }

    private void checkForLocationPermission() {
        permissionHelper = new PermissionHelper(this, new String[]{Permissions.FINE_LOCATION, Permissions.COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                fetchUserLocation();
            }

            @Override
            public void onPermissionDenied() {
                Toast.makeText(getContext(), getContext().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void fetchUserLocation() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()){
            LocationHelper locationHelper = new LocationHelper(activity);
            locationHelper.setLocationChangeListener(this);
            locationHelper.getLastKnownLocation(activity);
        }
    }

    private void observeDeliveryOrders(Location location) {
        viewModel.getDeliveryOrders(DropOffDeliveryOrdersViewModel.TYPE_CUSTOMER,
                DropOffDeliveryOrdersViewModel.STATUS_IN_TRANSIT).observe(
                DeliveryOrdersMapFragment.this, deliveryOrders -> {
                    if (deliveryOrders.size() > 0) {
                        viewModel.fetchTimeToReachAndUpdateDeliveryOrders(deliveryOrders, location, timeToReachCallback);
                    }
                    llMapAddressLayout.setVisibility(deliveryOrders.size() > 0 ? View.VISIBLE : View.GONE);
                });
    }

    private void displayMarkersOnMap(List<DeliveryOrderEntity> deliveryOrders) {
        mGoogleMap.clear();
        int count = 0;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < deliveryOrders.size(); i++) {
            DeliveryOrderEntity deliveryOrder = deliveryOrders.get(i);
            MarkerOptions markerOptions = viewModel.getMarkerOption(deliveryOrder);
            if (markerOptions != null) {
                Marker marker = mGoogleMap.addMarker(markerOptions);
                marker.setTag(deliveryOrder);
                builder.include(marker.getPosition());
                count++;
                if (i == 0) {
                    onMarkerClick(marker);
                }
            }
        }
        if (count == 0) return;
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
            Activity activity = getActivity();
            if (activity == null || activity.isFinishing()) return;
            activity.runOnUiThread(() -> {
                List<Distance> timeToReachList = (List<Distance>) uiModels;
                viewModel.setTimeToReachDistanceMatrix(timeToReachList);
                List<DeliveryOrderEntity> deliveryOrders = viewModel.getDeliveryOrders();
                if (deliveryOrders.size() == timeToReachList.size()) {
                    for (int i = 0; i < deliveryOrders.size() && i < timeToReachList.size(); i++) {
                        DeliveryOrderEntity deliveryOrder = deliveryOrders.get(i);
                        Distance distance = timeToReachList.get(i);
                        deliveryOrder.setDistanceFromCurrentLocation(distance);
                    }
                    displayMarkersOnMap((deliveryOrders));
                }

            });
        }
    };

    @Override
    public boolean onMarkerClick(Marker marker) {
        viewModel.setSelectedMarker(marker);
        DeliveryOrderEntity deliveryOrder = (DeliveryOrderEntity) marker.getTag();
        if (deliveryOrder != null) {
            tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
            tvDONumber.setText(deliveryOrder.getDoNumber() == null ? "-" : deliveryOrder.getDoNumber());
            tvAddress.setText(viewModel.getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2()));
            tvDeliveryDate.setText(viewModel.dateToDisplay(deliveryOrder.getPreferredDeliveryDate()));
            tvPreferredTime.setText(viewModel.timeToDisplay(deliveryOrder.getPreferredDeliveryTime()));
            tvTimeToReach.setText(viewModel.getEstimatedDeliveryTimeText(deliveryOrder.getDistanceFromCurrentLocation()));
            updateMarkers(marker, deliveryOrder);
            viewModel.setPreviouslySelectedMarker(viewModel.getSelectedMarker());
        }
        return true;
    }

    private void updateMarkers(Marker marker, DeliveryOrderEntity deliveryOrder) {
        Marker previousMarker = viewModel.getPreviouslySelectedMarker();
        if (previousMarker != null && previousMarker != viewModel.getSelectedMarker()) {
            DeliveryOrderEntity prevMarkerDO = (DeliveryOrderEntity) previousMarker.getTag();
            if (prevMarkerDO != null) {
                previousMarker.setIcon(BitmapDescriptorFactory.fromBitmap(viewModel.getMarkerBitmapFromView(prevMarkerDO.getDistanceFromCurrentLocation().getDurationText(), false)));
            }
        }
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(viewModel.getMarkerBitmapFromView(deliveryOrder.getDistanceFromCurrentLocation().getDurationText(), true)));
    }

    private void openDirectionsOnGoogleMaps() {
        Marker selectedMarker = viewModel.getSelectedMarker();
        if (selectedMarker != null) {
            LatLng destinationLatLng = selectedMarker.getPosition();
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(destinationLatLng.latitude) + "," +
                    String.valueOf(destinationLatLng.longitude));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    private void onMarkerDetailsTap(){
        Marker selectedMarker = viewModel.getSelectedMarker();
        if (selectedMarker == null) return;
        Object markerTag = selectedMarker.getTag();
        if (markerTag == null || !(markerTag instanceof DeliveryOrderEntity)) return;
        DeliveryOrderEntity selectedDO = (DeliveryOrderEntity) markerTag;
        openDeliveryDetailsActivity(selectedDO.getId());
    }

    private void openDeliveryDetailsActivity(int deliveryOrderId) {
        Intent doDetailsIntent = new Intent(getActivity(), DropOffDeliveryOrderDetailsActivity.class);
        doDetailsIntent.putExtra(IntentConstants.DELIVERY_ORDER_ID, deliveryOrderId);
        startActivity(doDetailsIntent);
    }

    @Override
    public void onDestroy() {
        permissionHelper = null;
        super.onDestroy();
    }
}
