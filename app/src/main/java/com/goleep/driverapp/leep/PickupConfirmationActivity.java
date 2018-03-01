package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.viewmodels.PickupDeliveryOrderViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickupConfirmationActivity extends ParentAppCompatActivity {
    @BindView(R.id.warehouse_info_text_view)
    CustomTextView wareHouseInfoTextView;
    private PickupDeliveryOrderViewModel pickupDeliveryOrderViewModel;

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        pickupDeliveryOrderViewModel = ViewModelProviders.of(
                PickupConfirmationActivity.this).get(PickupDeliveryOrderViewModel.class);
        initView();
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.pickup_stock), R.drawable.ic_pickup_toolbar);
        setWareHouseDetails();
    }

    private void setWareHouseDetails() {
        wareHouseInfoTextView.setText(pickupDeliveryOrderViewModel.getWareHouseNameAddress());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_pickup_confirmation);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
        }
    }
}
