package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.fragments.DropoffReturnedFragment;
import com.goleep.driverapp.fragments.DropoffSellableItemsFragment;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uihelpers.NonSwipeableViewPager;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.DropoffViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropoffActivity extends ParentAppCompatActivity {

    private DropoffViewModel dropoffViewModel;
    @BindView(R.id.pickup_view_pager)
    public NonSwipeableViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.warehouse_info_text_view)
    CustomTextView wareHouseInfoTextView;
    @BindView(R.id.map_button)
    LinearLayout mapButton;
    @BindView(R.id.from_text_view)
    CustomTextView fromTextView;

    private View.OnClickListener nextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewPager.setCurrentItem(1);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_dropoff);
        fromTextView = findViewById(R.id.from_text_view);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        dropoffViewModel = ViewModelProviders.of(this).get(DropoffViewModel.class);
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
        mapButton.setVisibility(View.GONE);
        fromTextView.setText(getString(R.string.to));
        wareHouseInfoTextView.setText(dropoffViewModel.getWareHouseNameAddress());
    }

    private void initialiseTabBar() {
        DropoffPagerAdapter deliveryOrderPagerAdapter = new DropoffPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(deliveryOrderPagerAdapter);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        View returnableTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        CustomTextView textView = returnableTab.findViewById(R.id.title_text);
        ImageView icon = returnableTab.findViewById(R.id.icon);
        returnableTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        icon.setVisibility(View.GONE);
        textView.setText(getString(R.string.returned));

        tabLayout.getTabAt(0).setCustomView(returnableTab);

        View sellableTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        textView = sellableTab.findViewById(R.id.title_text);
        icon = sellableTab.findViewById(R.id.icon);
        sellableTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        icon.setVisibility(View.GONE);
        textView.setText(getString(R.string.sellable));

        tabLayout.getTabAt(1).setCustomView(sellableTab);
    }

    private void processIntent() {
        int locationId = getIntent().getIntExtra(IntentConstants.WAREHOUSE_ID, -1);
        if(locationId != -1)
            dropoffViewModel.setWarehouse(dropoffViewModel.getWarehouse(locationId));
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                AppUtils.hideKeyboard(this.getCurrentFocus());
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == AppConstants.ACTIVITY_SUCCESS_RESULT) {
                finish();
            }
        }
    }

    public void setSelectedReturnableIds(ArrayList<Integer> selectedReturnableIds){
        dropoffViewModel.setSelectedReturnableIds(selectedReturnableIds);
    }

    public ArrayList<Integer> getSelectedReturnableIds(){
        return (ArrayList<Integer>) dropoffViewModel.getSelectedReturnableIds();
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
                    dropoffReturnedFragment.setNextClickListener(nextClickListener);
                    Bundle bundle = new Bundle();
                    dropoffReturnedFragment.setArguments(bundle);
                    return dropoffReturnedFragment;
                case 1:
                    DropoffSellableItemsFragment dropoffSellableItemsFragment = new DropoffSellableItemsFragment();
                    Bundle dropoffSellableItemsFragmentBundle = new Bundle();
                    dropoffSellableItemsFragmentBundle.putInt(IntentConstants.WAREHOUSE_ID,
                            dropoffViewModel.getWarehouse().getId());
                    dropoffSellableItemsFragment.setArguments(dropoffSellableItemsFragmentBundle);
                    return dropoffSellableItemsFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_ITEMS;
        }
    }

}
