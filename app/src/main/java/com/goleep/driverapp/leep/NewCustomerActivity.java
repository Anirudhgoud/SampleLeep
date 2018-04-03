package com.goleep.driverapp.leep;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uihelpers.LocationHelper;
import com.goleep.driverapp.helpers.uimodels.MapAttribute;
import com.goleep.driverapp.helpers.uimodels.ReportAttrribute;
import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.utils.LogUtils;
import com.goleep.driverapp.viewmodels.NewCustomerViewModel;
import com.goleep.driverapp.viewmodels.ReportsViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewCustomerActivity extends ParentAppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationChangeListener, GoogleMap.OnMarkerDragListener {
    @BindView(R.id.tv_complete_address)
    CustomTextView tvAddress;
    @BindView(R.id.et_location)
    CustomEditText etLocation;
    @BindView(R.id.et_postalcode)
    CustomEditText etPostalCode;
    @BindView(R.id.et_addressline1)
    CustomEditText etAddressLine1;
    @BindView(R.id.et_addressline2)
    CustomEditText etAddressLine2;
    @BindView(R.id.et_city)
    CustomEditText etCity;
    @BindView(R.id.et_state)
    CustomEditText etState;
    @BindView(R.id.et_country)
    CustomEditText etCountry;
    @BindView(R.id.tv_confirm)
    CustomTextView tvConfirm;
    private GoogleMap googleMap;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private Marker marker;
    private NewCustomerViewModel newCustomerViewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_new_customer);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(NewCustomerActivity.this);
        newCustomerViewModel = ViewModelProviders.of(NewCustomerActivity.this).get(NewCustomerViewModel.class);
        initialiseMapView();
    }

    @Override
    public void onClickWithId(int resourceId) {

    }

    private void initialiseMapView() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(NewCustomerActivity.this);
    }

    private UILevelNetworkCallback mapCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            dismissProgressDialog();
            runOnUiThread(() -> handleReportsResponse(uiModels, isDialogToBeShown, errorMessage, toLogout));
        }
    };

    private void handleReportsResponse(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
        dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else if (isDialogToBeShown) {
                showNetworkRelatedDialogs(errorMessage);
            }
        } else if (uiModels.size() > 0) {
            runOnUiThread(() -> {
                MapAttribute mapAttribute = (MapAttribute) uiModels.get(0);
                setUiElements(mapAttribute);
            });
        }
    }


    private void checkForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(NewCustomerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewCustomerActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        onPermissionGranted();
    }

    private void onPermissionGranted() {
        fetchUserLocation();
    }

    private void fetchUserLocation() {
        LocationHelper locationHelper = new LocationHelper(NewCustomerActivity.this);
        locationHelper.setLocationChangeListener(NewCustomerActivity.this);
        locationHelper.getLastKnownLocation(NewCustomerActivity.this);
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted();
                } else {
                    Toast.makeText(NewCustomerActivity.this, NewCustomerActivity.this.getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        NewCustomerActivity.this.googleMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkForLocationPermission();
        else
            fetchUserLocation();

    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {}
    @Override
    public void onLastKnownLocationReceived(Location location) {
        googleMap.setOnMarkerDragListener(this);
        if (location == null) {
            Toast.makeText(NewCustomerActivity.this, NewCustomerActivity.this.getString(R.string.location_fetch_error), Toast.LENGTH_SHORT).show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    getAddressFromLatLng(location.getLatitude(), location.getLongitude());
                    LogUtils.debug("newCusto", latLng + "");
                    marker = googleMap.addMarker(new
                            MarkerOptions()
                            .position(latLng)
                            .title("your location").draggable(true));
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_selected));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            });
        }

    }
    @Override
    public void onLastKnownLocationError(String errorMessage) {
        Toast.makeText(NewCustomerActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    private void getAddressFromLatLng(double latitude, double longitude) {
        showProgressDialog();
        newCustomerViewModel.getAddressFromLatitudeLongitude(mapCallBack, String.valueOf(latitude), String.valueOf(longitude));
    }
    @Override
    public void onMarkerDragStart(Marker marker) {}
    @Override
    public void onMarkerDrag(Marker marker) {}
    @Override
    public void onMarkerDragEnd(Marker marker) {
        LogUtils.debug("new Cust", marker.getPosition().latitude + " " + marker.getPosition().longitude);
        getAddressFromLatLng(marker.getPosition().latitude, marker.getPosition().longitude);
    }
    private void setUiElements(MapAttribute mapAttribute) {
        marker.setTitle(mapAttribute.getTotalAddress());
        tvAddress.setText(mapAttribute.getTotalAddress());
        etPostalCode.setText(mapAttribute.getPostalCode());
        etAddressLine1.setText(mapAttribute.getAddressLine1());
        etAddressLine2.setText(mapAttribute.getAddressLine2());
        etCity.setText(mapAttribute.getCity());
        etState.setText(mapAttribute.getState());
        etCountry.setText(mapAttribute.getCountry());
    }

}
