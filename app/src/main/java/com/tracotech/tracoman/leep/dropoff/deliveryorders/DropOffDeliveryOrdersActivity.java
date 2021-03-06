package com.tracotech.tracoman.leep.dropoff.deliveryorders;

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
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.fragments.DeliveryOrdersListFragment;
import com.tracotech.tracoman.fragments.DeliveryOrdersMapFragment;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.viewmodels.dropoff.deliveryorders.DropOffDeliveryOrdersViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropOffDeliveryOrdersActivity extends ParentAppCompatActivity {

    private DropOffDeliveryOrdersViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(DropOffDeliveryOrdersViewModel.class);
        initialiseToolbar();
        initialiseTabBar();
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.delivery_orders), R.drawable.ic_drop_off_toolbar);
    }

    private void initialiseTabBar() {
        DeliveryOrderPagerAdapter deliveryOrderPagerAdapter = new DeliveryOrderPagerAdapter(getSupportFragmentManager());
        doViewPager.setAdapter(deliveryOrderPagerAdapter);
        tabLayout.setupWithViewPager(doViewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        View listTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        TextView textView = listTab.findViewById(R.id.title_text);
        ImageView icon = listTab.findViewById(R.id.icon);
        listTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.list));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_tab));
        TabLayout.Tab tabList = tabLayout.getTabAt(0);
        if (tabList != null) tabList.setCustomView(listTab);

        View mapTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        textView = mapTab.findViewById(R.id.title_text);
        icon = mapTab.findViewById(R.id.icon);
        mapTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.map));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_map_tab));
        TabLayout.Tab tabMap = tabLayout.getTabAt(1);
        if (tabMap != null) tabMap.setCustomView(mapTab);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;
        }
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
