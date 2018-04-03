package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.WarehousePagerAdapter;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.viewmodels.DropoffViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropoffWarehouseActivity extends ParentAppCompatActivity {

    private DropoffViewModel dropoffViewModel;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_dropoff_warehouse);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        dropoffViewModel = ViewModelProviders.of(DropoffWarehouseActivity.this).get(DropoffViewModel.class);
        initView();
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.dropoff), R.drawable.ic_drop_off_title);
        initialiseTabBar();
    }

    private void initialiseTabBar() {
        WarehousePagerAdapter warehousePagerAdapter = new WarehousePagerAdapter(getSupportFragmentManager(),
                new String[]{getString(R.string.list), getString(R.string.map), getString(R.string.app_name)});
        viewPager.setAdapter(warehousePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        View listTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        CustomTextView textView = listTab.findViewById(R.id.title_text);
        ImageView icon = listTab.findViewById(R.id.icon);
        listTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.list));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_tab));
        tabLayout.getTabAt(0).setCustomView(listTab);
        View mapTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        textView = mapTab.findViewById(R.id.title_text);
        icon = mapTab.findViewById(R.id.icon);
        mapTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.map));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_map_tab));
        tabLayout.getTabAt(1).setCustomView(mapTab);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }
}
