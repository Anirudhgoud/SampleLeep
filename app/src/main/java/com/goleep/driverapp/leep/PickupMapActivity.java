package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.viewmodels.PickupMapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        PickupMapActivity.this.googleMap = googleMap;
        LatLng warehouseLatLng = pickupMapViewModel.getWarehouseLatLng();
        if(warehouseLatLng != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    warehouseLatLng).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.addMarker(new MarkerOptions().position(warehouseLatLng).icon(BitmapDescriptorFactory
                    .fromBitmap(getMarkerBitmapFromView("39"))));
        }
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(PickupMapActivity.this);
        pickupMapViewModel = ViewModelProviders.of(PickupMapActivity.this).get(PickupMapViewModel.class);
        requestLocationsPermission();
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_pickup_map);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
        }
    }

    private Bitmap getMarkerBitmapFromView(String timeToReach) {
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.map_marker_title_layout, null);
        ((CustomTextView)customMarkerView.findViewById(R.id.time_to_reach_tv)).setText(timeToReach);
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
