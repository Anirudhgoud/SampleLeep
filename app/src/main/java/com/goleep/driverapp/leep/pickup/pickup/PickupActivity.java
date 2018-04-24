package com.goleep.driverapp.leep.pickup.pickup;

import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.fragments.PickupCashSalessFragment;
import com.goleep.driverapp.fragments.PickupDeliveryOrderFragment;
import com.goleep.driverapp.helpers.uihelpers.NonSwipeableViewPager;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.interfaces.ItemCheckListener;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.viewmodels.WarehouseDetailsViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickupActivity extends ParentAppCompatActivity implements ItemCheckListener{

    @BindView(R.id.pickup_view_pager)
    public NonSwipeableViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.warehouse_info_text_view)
    TextView wareHouseInfoTextView;
    @BindView(R.id.map_button)
    LinearLayout mapButton;
    private WarehouseDetailsViewModel warehouseDetailsViewModel;
    private List<Integer> selectedDeliveryOrders = new ArrayList<>();
    private List<Integer> cashDoItems = new ArrayList<>();

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        warehouseDetailsViewModel = ViewModelProviders.of(PickupActivity.this).get(WarehouseDetailsViewModel.class);
        processIntent();
        initView();
    }

    private void processIntent() {
        int locationId = getIntent().getIntExtra(IntentConstants.WAREHOUSE_ID, -1);
        if(locationId != -1)
            warehouseDetailsViewModel.setWarehouse(locationId);
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
        mapButton.setVisibility(View.GONE);
    }

    private void setWareHouseDetails() {
        wareHouseInfoTextView.setText(warehouseDetailsViewModel.getWareHouseNameAddress());
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
        TextView textView = doTab.findViewById(R.id.title_text);
        ImageView icon = doTab.findViewById(R.id.icon);
        doTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.delivery_order));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.delivery_orders_tab));
        TabLayout.Tab tabDo = tabLayout.getTabAt(0);
        if (tabDo != null) tabDo.setCustomView(doTab);
        View cashSalesTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        textView = cashSalesTab.findViewById(R.id.title_text);
        icon = cashSalesTab.findViewById(R.id.icon);
        cashSalesTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(getString(R.string.cash_sales));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.cash_sales_tab));
        TabLayout.Tab tabCashSales = tabLayout.getTabAt(1);
        if (tabCashSales != null) tabCashSales.setCustomView(cashSalesTab);
    }

    @Override
    public void itemChecked(BaseListItem item, boolean checked) {
        if (item instanceof OrderItemEntity) {
            if (checked && !cashDoItems.contains(((OrderItemEntity) item).getId()) && item.getItemType()
                    == AppConstants.TYPE_CASH_SALES_ITEM) {
                cashDoItems.add(((OrderItemEntity) item).getId());
            } else if (!checked && cashDoItems.contains(((OrderItemEntity) item).getId()) && item.getItemType()
                    == AppConstants.TYPE_CASH_SALES_ITEM) {
                cashDoItems.removeAll(Collections.singletonList(((OrderItemEntity) item).getId()));
            }
        } else if (item instanceof DeliveryOrderEntity) {
            if (checked && !selectedDeliveryOrders.contains(((DeliveryOrderEntity) item).getId())) {
                selectedDeliveryOrders.add(((DeliveryOrderEntity) item).getId());
            } else if (selectedDeliveryOrders.contains(((DeliveryOrderEntity) item).getId())) {
                selectedDeliveryOrders.removeAll(Collections.singletonList(((DeliveryOrderEntity) item).getId()));
            }
        }
    }

    public List<Integer> getSelectedCashSalesIds() {
        return cashDoItems;
    }

    public List<Integer> getSelectedDoIds() {
        return selectedDeliveryOrders;
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
                    Bundle bundle = new Bundle();
                    bundle.putInt(IntentConstants.WAREHOUSE_ID, warehouseDetailsViewModel.getWarehouse().getId());
                    pickupDeliveryOrderFragment.setArguments(bundle);
                    return pickupDeliveryOrderFragment;
                case 1:
                    PickupCashSalessFragment pickupCashSalessFragment = new PickupCashSalessFragment();
                    Bundle pickupCashSalessFragmentBundle = new Bundle();
                    pickupCashSalessFragmentBundle.putInt(IntentConstants.WAREHOUSE_ID, warehouseDetailsViewModel.getWarehouse().getId());
                    pickupCashSalessFragment.setArguments(pickupCashSalessFragmentBundle);
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

    }
}
