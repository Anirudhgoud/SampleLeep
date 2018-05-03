package com.tracotech.tracoman.leep.dropoff.cashsales;

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
import com.tracotech.tracoman.fragments.CashSalesExistingCustomerFragment;
import com.tracotech.tracoman.fragments.CashSalesNewCustomerFragment;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;

public class CashSalesActivity extends ParentAppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_cash_sales);
    }

    @Override
    public void doInitialSetup() {
        connectUIElements();
        initialiseToolbar();
        initialiseTabBar();
    }

    private void connectUIElements() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.cash_sales), R.drawable.ic_cash_sales);
    }

    private void initialiseTabBar() {
        CashSalesPagerAdapter cashSalesPagerAdapter = new CashSalesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(cashSalesPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        View existingCustomerTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        TextView textView = existingCustomerTab.findViewById(R.id.title_text);
        ImageView icon = existingCustomerTab.findViewById(R.id.icon);
        existingCustomerTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.existing_customer));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_existing_customer));
        TabLayout.Tab tabExistingCustomer = tabLayout.getTabAt(0);
        if (tabExistingCustomer != null) tabExistingCustomer.setCustomView(existingCustomerTab);

        View newCustomerTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        textView = newCustomerTab.findViewById(R.id.title_text);
        icon = newCustomerTab.findViewById(R.id.icon);
        newCustomerTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.new_customer));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_new_customer));
        TabLayout.Tab tabNewCustomer = tabLayout.getTabAt(1);
        if (tabNewCustomer != null) tabNewCustomer.setCustomView(newCustomerTab);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }

    class CashSalesPagerAdapter extends FragmentPagerAdapter {

        private int NUMBER_OF_ITEMS = 2;

        public CashSalesPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CashSalesExistingCustomerFragment();
                case 1:
                    return new CashSalesNewCustomerFragment();
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
                    return getResources().getString(R.string.existing_customer);
                case 1:
                    return getResources().getString(R.string.new_customer);
                default:
                    return getResources().getString(R.string.app_name);
            }
        }
    }
}
