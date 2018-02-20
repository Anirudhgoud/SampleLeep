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
import com.goleep.driverapp.fragments.DeliveryOrdersListFragment;
import com.goleep.driverapp.fragments.DeliveryOrdersMapFragment;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
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
        tabLayout.setupWithViewPager(doViewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        CustomTextView textView = view.findViewById(R.id.title_text);
        ImageView icon = view.findViewById(R.id.icon);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        textView.setText(getString(R.string.delivery_order));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_tab));


        tabLayout.getTabAt(0).setCustomView(view);

        View mapTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        textView = mapTab.findViewById(R.id.title_text);
        icon = mapTab.findViewById(R.id.icon);
        mapTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.cash_sales));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_map_tab));
        tabLayout.getTabAt(1).setCustomView(mapTab);
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
