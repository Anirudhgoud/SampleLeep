package com.goleep.driverapp.leep;

import android.Manifest;
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
import com.goleep.driverapp.interfaces.LocationChangeListener;
import com.goleep.driverapp.utils.LogUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewCustomerActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationChangeListener , GoogleMap.OnMarkerDragListener{
    @BindView(R.id.tv_complete_address)
    CustomTextView tvAddress;
    @BindView(R.id.et_location)
    CustomEditText etLocation;
    @BindView(R.id.et_postalcode)
    CustomEditText etPostalCode;
    @BindView(R.id.et_addressline1)
    CustomEditText etAddressLine1;
    @BindView(R.id.et_addressline2)
    CustomEditText getEtAddressLine2;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        ButterKnife.bind(NewCustomerActivity.this);
        initialiseMapView();
    }

    private void initialiseMapView() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(NewCustomerActivity.this);
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
        LatLng latLng = new LatLng(21, 57);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkForLocationPermission();
        else
            fetchUserLocation();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLastKnownLocationReceived(Location location) {
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
                            .title("your location"));
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_selected));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
/*
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(
                            latLng).zoom(15).build();*/

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

    private void getAddressFromLatLng(double lat, double lng) {
        String strAddress, city, country, postalCode;

        Geocoder geocoder;
        List<Address> ListAddresses;
        geocoder = new Geocoder(NewCustomerActivity.this, Locale.getDefault());
        try {
            ListAddresses = geocoder.getFromLocation(lat, lng, 3); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Address address = ListAddresses.get(0);
            LogUtils.debug("newCust", address + "");
            strAddress = address.getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = address.getLocality();
            country = address.getCountryName();
            postalCode = address.getPostalCode();
            etAddressLine1.setText(address.getFeatureName() + " " + address.getThoroughfare());
            getEtAddressLine2.setText(address.getPostalCode() + " " + address.getSubAdminArea() + " " + address.getLocality());
            tvAddress.setText(strAddress);
            etCity.setText(city);
            etState.setText(address.getAdminArea());
            etCountry.setText(country);
            etPostalCode.setText(postalCode);

        } catch (Exception e) {
            Toast.makeText(NewCustomerActivity.this, "could not find your location", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
