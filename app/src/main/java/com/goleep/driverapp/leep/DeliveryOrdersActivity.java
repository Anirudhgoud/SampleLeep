package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.goleep.driverapp.R;
import com.goleep.driverapp.fragments.DeliveryOrdersListFragment;
import com.goleep.driverapp.fragments.DeliveryOrdersMapFragment;
import com.goleep.driverapp.viewmodels.DeliveryOrdersViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryOrdersActivity extends ParentAppCompatActivity {

    DeliveryOrdersViewModel viewModel;

    @BindView(R.id.do_view_pager)
    ViewPager doViewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_delivery_orders);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(DeliveryOrdersViewModel.class);
        initialiseTabBar();
    }

    private void initialiseTabBar() {
        DeliveryOrderPagerAdapter deliveryOrderPagerAdapter = new DeliveryOrderPagerAdapter(getSupportFragmentManager());
        doViewPager.setAdapter(deliveryOrderPagerAdapter);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(doViewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_list_tab).setCustomView(R.layout.custom_tab_item_layout);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_map_tab).setCustomView(R.layout.custom_tab_item_layout);
    }

    @Override
    public void onClickWithId(int resourceId) {

    }


    class DeliveryOrderPagerAdapter extends FragmentPagerAdapter {

        private int NUMBER_OF_ITEMS = 2;

        public DeliveryOrderPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DeliveryOrdersListFragment();
                case 1:
                    return new DeliveryOrdersMapFragment();
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
                    return getResources().getString(R.string.list);
                case 1:
                    return getResources().getString(R.string.map);
                default:
                    return getResources().getString(R.string.app_name);
            }
        }
    }

}
