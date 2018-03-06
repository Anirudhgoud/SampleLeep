package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
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
import com.goleep.driverapp.helpers.uihelpers.NonSwipeableViewPager;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.interfaces.ItemCheckListener;
import com.goleep.driverapp.services.room.RoomDBService;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.Product;
import com.goleep.driverapp.viewmodels.PickupViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickupActivity extends ParentAppCompatActivity implements ItemCheckListener{

    @BindView(R.id.pickup_view_pager)
    public NonSwipeableViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.warehouse_info_text_view)
    CustomTextView wareHouseInfoTextView;
    private PickupViewModel pickupViewModel;
    private List<Integer> selectedDeliveryOrders = new ArrayList<>();
    private List<Integer> cashDoItems = new ArrayList<>();

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        pickupViewModel = ViewModelProviders.of(PickupActivity.this).get(PickupViewModel.class);
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_pickup);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
            break;
        }
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.pickup_stock), R.drawable.ic_pickup_toolbar);
        initialiseTabBar();
        setWareHouseDetails();
    }

    private void setWareHouseDetails() {
        wareHouseInfoTextView.setText(pickupViewModel.getWareHouseNameAddress());
    }

    private void initialiseTabBar() {
        PickupPagerAdapter deliveryOrderPagerAdapter = new PickupPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(deliveryOrderPagerAdapter);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    private void setupTabIcons() {
        View doTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        CustomTextView textView = doTab.findViewById(R.id.title_text);
        ImageView icon = doTab.findViewById(R.id.icon);
        doTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        textView.setText(getString(R.string.delivery_order));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.delivery_orders_tab));
        doTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tabLayout.getTabAt(0).setCustomView(doTab);

        View cashSalesTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        textView = cashSalesTab.findViewById(R.id.title_text);
        icon = cashSalesTab.findViewById(R.id.icon);
        cashSalesTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.cash_sales));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.cash_sales_tab));
        cashSalesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tabLayout.getTabAt(1).setCustomView(cashSalesTab);
    }

    public void logoutUser(){
        super.logoutUser();
    }

    @Override
    public void itemChecked(BaseListItem item, boolean checked) {
        if(item instanceof DeliveryOrderItem)
            cashDoItems.add(((DeliveryOrderItem) item).getId());
        else if(item instanceof DeliveryOrder) {
            if(checked)
                selectedDeliveryOrders.add(((DeliveryOrder) item).getId());
            else if(selectedDeliveryOrders.contains(((DeliveryOrder) item).getId())){
                selectedDeliveryOrders.remove(((DeliveryOrder) item).getId());
            }
        }
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
                    PickupDeliveryOrderFragment pickupDeliveryOrderFragment = new PickupDeliveryOrderFragment();
                    pickupDeliveryOrderFragment.setItemSelectionListener(PickupActivity.this);
                    return pickupDeliveryOrderFragment;
                case 1:
                    PickupCashSalessFragment pickupCashSalessFragment = new PickupCashSalessFragment();
                    pickupCashSalessFragment.setItemSelectionListener(PickupActivity.this);
                    return pickupCashSalessFragment;
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
