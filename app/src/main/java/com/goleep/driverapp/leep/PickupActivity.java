package com.goleep.driverapp.leep;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.fragments.PickupCashSalessFragment;
import com.goleep.driverapp.fragments.PickupDeliveryOrderFragment;
import com.goleep.driverapp.helpers.customfont.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickupActivity extends ParentAppCompatActivity {

    @BindView(R.id.pickup_view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;


    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        initView();
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_pickup);
    }

    @Override
    public void onClickWithId(int resourceId) {

    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        initialiseTabBar();

    }

    private void initialiseTabBar() {
        PickupPagerAdapter deliveryOrderPagerAdapter = new PickupPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(deliveryOrderPagerAdapter);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        CustomTextView textView = view.findViewById(R.id.title_text);
        ImageView icon = view.findViewById(R.id.icon);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        textView.setText(getString(R.string.delivery_order));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.delivery_orders_tab));


        tabLayout.getTabAt(0).setCustomView(view);

        View mapTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        textView = mapTab.findViewById(R.id.title_text);
        icon = mapTab.findViewById(R.id.icon);
        mapTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.cash_sales));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.cash_sales_tab));
        tabLayout.getTabAt(1).setCustomView(mapTab);
    }

    class PickupPagerAdapter extends FragmentPagerAdapter {

        private int NUMBER_OF_ITEMS = 2;

        public PickupPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PickupDeliveryOrderFragment();
                case 1:
                    return new PickupCashSalessFragment();
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
