package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.goleep.driverapp.R;

import com.goleep.driverapp.fragments.WarehouseListFragment;
import com.goleep.driverapp.fragments.WarehouseMapFragment;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.viewmodels.DeliveryOrderViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickupWarehouseActivity extends ParentAppCompatActivity {

    DeliveryOrderViewModel deliveryOrderViewModel;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_pickup_warehouse);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(PickupWarehouseActivity.this);
        deliveryOrderViewModel = ViewModelProviders.of(PickupWarehouseActivity.this).
                get(DeliveryOrderViewModel.class);
        initView();
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.pickup_stock), R.drawable.ic_pickup_toolbar);
        initialiseTabBar();
    }

    private void initialiseTabBar() {
        WarehousePagerAdapter warehousePagerAdapter = new WarehousePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(warehousePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        View listTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        CustomTextView textView = listTab.findViewById(R.id.title_text);
        ImageView icon = listTab.findViewById(R.id.icon);
        listTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        textView.setText(getString(R.string.list));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_tab));


        tabLayout.getTabAt(0).setCustomView(listTab);

        View mapTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        textView = mapTab.findViewById(R.id.title_text);
        icon = mapTab.findViewById(R.id.icon);
        mapTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.map));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_map_tab));

        tabLayout.getTabAt(1).setCustomView(mapTab);
    }



    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
        }
    }


    class WarehousePagerAdapter extends FragmentPagerAdapter {

        private int NUMBER_OF_ITEMS = 2;

        public WarehousePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new WarehouseListFragment();
                case 1:
                    return new WarehouseMapFragment();
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
