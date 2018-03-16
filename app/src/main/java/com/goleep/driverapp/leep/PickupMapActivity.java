package com.goleep.driverapp.leep;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.interfaces.OnPermissionResult;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.viewmodels.PickupMapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickupMapActivity extends ParentAppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;

    private PickupMapViewModel pickupMapViewModel;

    @BindView(R.id.warehouse_info_text_view)
    CustomTextView wareHouseInfoTextView;
    @BindView(R.id.location_name_tv)
    CustomTextView locationName;
    @BindView(R.id.location_address_tv)
    CustomTextView locationAddress;
    @BindView(R.id.time_to_reach_tv)
    CustomTextView timeToReach;
    @BindView(R.id.navigate_iv)
    ImageView navigateIcon;

    private UILevelNetworkCallback timeToReachCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if(uiModels.size() > 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeToReach.setText((CharSequence) uiModels.get(0));
                        googleMap.addMarker(new MarkerOptions().position(pickupMapViewModel.getWarehouseLatLng())
                                .icon(BitmapDescriptorFactory
                                .fromBitmap(getMarkerBitmapFromView((String) uiModels.get(0)))));
                    }
                });
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        PickupMapActivity.this.googleMap = googleMap;
        LatLng warehouseLatLng = pickupMapViewModel.getWarehouseLatLng();
        if (warehouseLatLng != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    warehouseLatLng).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(PickupMapActivity.this);
        pickupMapViewModel = ViewModelProviders.of(PickupMapActivity.this).get(PickupMapViewModel.class);
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.pickup_stock), R.drawable.ic_pickup_toolbar);
        initView();
    }



    private void initView() {
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        wareHouseInfoTextView.setText(pickupMapViewModel.getWareHouseNameAddress());
        locationName.setText(pickupMapViewModel.getWareHouseName());
        locationAddress.setText(pickupMapViewModel.getWarehouseAddress());
        LatLng warehouseLatLng = pickupMapViewModel.getWarehouseLatLng();
        navigateIcon.setOnClickListener(this);
        if(isPermissionGranted( new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION})) {
            LatLng userLatLng = pickupMapViewModel.getUserLocation();
            pickupMapViewModel.getTimeToReach(userLatLng, warehouseLatLng, timeToReachCallback);

        } else {
            requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, new OnPermissionResult() {
                @Override
                public void onPermissionGranted() {
                    LatLng userLatLng = pickupMapViewModel.getUserLocation();
                    pickupMapViewModel.getTimeToReach(userLatLng, warehouseLatLng, timeToReachCallback);
                    //navigateIcon.setOnClickListener(this);
                }

                @Override
                public void onPermissionDenied() {

                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_pickup_map);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;
            case R.id.navigate_iv : openMaps();
            break;
        }
    }

    private void openMaps() {
        LatLng warehouseLatLng = pickupMapViewModel.getWarehouseLatLng();
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+String.valueOf(warehouseLatLng.latitude)+","+
                String.valueOf(warehouseLatLng.longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private Bitmap getMarkerBitmapFromView(String timeToReach) {
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
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
}
