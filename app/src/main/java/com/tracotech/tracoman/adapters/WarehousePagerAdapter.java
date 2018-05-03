package com.tracotech.tracoman.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.fragments.WarehouseListFragment;
import com.tracotech.tracoman.fragments.WarehouseMapFragment;

/**
 * Created by vishalm on 30/03/18.
 */

public class WarehousePagerAdapter extends FragmentPagerAdapter {

    private int NUMBER_OF_ITEMS = 2;
    private String[] titles;
    private boolean isPickup = false;

    public WarehousePagerAdapter(FragmentManager fragmentManager, String[] titles, boolean isPickup) {
        super(fragmentManager);
        this.titles = titles;
        this.isPickup = isPickup;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment fragment = new WarehouseListFragment();
                Bundle args = new Bundle();
                args.putBoolean(IntentConstants.IS_PICKUP, isPickup);
                fragment.setArguments(args);
                return fragment;
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
                return titles[1];
        }
    }
}
