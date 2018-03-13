package com.goleep.driverapp.leep;

import android.os.Bundle;

import com.goleep.driverapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickupMapActivity extends ParentAppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        PickupMapActivity.this.googleMap = googleMap;
        
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(PickupMapActivity.this);
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.pickup_stock), R.drawable.ic_pickup_toolbar);
        initView();
    }

    private void initView() {
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_pickup_map);
    }

    @Override
    public void onClickWithId(int resourceId) {

    }
}
