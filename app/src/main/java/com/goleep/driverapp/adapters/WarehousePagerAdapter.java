package com.goleep.driverapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.goleep.driverapp.fragments.WarehouseListFragment;
import com.goleep.driverapp.fragments.WarehouseMapFragment;

/**
 * Created by vishalm on 30/03/18.
 */

public class WarehousePagerAdapter extends FragmentPagerAdapter {

    private int NUMBER_OF_ITEMS = 2;
    private String[] titles;

    public WarehousePagerAdapter(FragmentManager fragmentManager, String[] titles) {
        super(fragmentManager);
        this.titles = titles;
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
                return titles[0];
            case 1:
                return titles[1];
            default:
                return titles[2];
        }
    }
}
