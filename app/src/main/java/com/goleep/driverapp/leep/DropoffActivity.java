package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.goleep.driverapp.R;import com.goleep.driverapp.fragments.DropoffReturnedFragment;
import com.goleep.driverapp.fragments.DropoffSellableItemsFragment;
import com.goleep.driverapp.helpers.uihelpers.NonSwipeableViewPager;
import com.goleep.driverapp.viewmodels.DropoffDeliveryOrderViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropoffActivity extends ParentAppCompatActivity {

    private DropoffDeliveryOrderViewModel dropoffDeliveryOrderViewModel;
    @BindView(R.id.pickup_view_pager)
    public NonSwipeableViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_dropoff);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        dropoffDeliveryOrderViewModel = ViewModelProviders.of(this).get(DropoffDeliveryOrderViewModel.class);
        processIntent();
        initView();
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.dropoff_stock), R.drawable.ic_drop_off_title);
        initialiseTabBar();
        setWareHouseDetails();
    }

    private void setWareHouseDetails() {

    }

    private void initialiseTabBar() {
        DropoffPagerAdapter deliveryOrderPagerAdapter = new DropoffPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(deliveryOrderPagerAdapter);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void processIntent() {
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }

    class DropoffPagerAdapter extends FragmentPagerAdapter {

        private int NUMBER_OF_ITEMS = 2;

        public DropoffPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    DropoffReturnedFragment dropoffReturnedFragment = new DropoffReturnedFragment();
                    //dropoffReturnedFragment.setItemSelectionListener(PickupActivity.this);
                    Bundle bundle = new Bundle();
                    //bundle.putInt(IntentConstants.WAREHOUSE_ID, pickupViewModel.getWarehouse().getId());
                    dropoffReturnedFragment.setArguments(bundle);
                    return dropoffReturnedFragment;
                case 1:
                    DropoffSellableItemsFragment dropoffSellableItemsFragment = new DropoffSellableItemsFragment();
                    Bundle pickupCashSalessFragmentBundle = new Bundle();
                    //pickupCashSalessFragmentBundle.putInt(IntentConstants.WAREHOUSE_ID, pickupViewModel.getWarehouse().getId());
                    dropoffSellableItemsFragment.setArguments(pickupCashSalessFragmentBundle);
                    //pickupCashSalessFragment.setItemSelectionListener(PickupActivity.this);
                    return dropoffSellableItemsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.returned);
                case 1:
                    return getResources().getString(R.string.sellable_stocks);
                default:
                    return getResources().getString(R.string.app_name);
            }
        }

    }

}
