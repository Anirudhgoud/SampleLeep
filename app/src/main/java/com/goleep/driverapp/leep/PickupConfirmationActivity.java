package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.DoExpandableListAdapter;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.Product;
import com.goleep.driverapp.viewmodels.PickupDeliveryOrderViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickupConfirmationActivity extends ParentAppCompatActivity {
    @BindView(R.id.warehouse_info_text_view)
    CustomTextView wareHouseInfoTextView;
    @BindView(R.id.expandable_list)
    RecyclerView expandableListView;
    private DoExpandableListAdapter adapter;
    private PickupDeliveryOrderViewModel pickupDeliveryOrderViewModel;
    ArrayList<DeliveryOrderItem> cashDoItems = new ArrayList<>();
    ArrayList<Product> cashProducts = new ArrayList<>();

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        pickupDeliveryOrderViewModel = ViewModelProviders.of(
                PickupConfirmationActivity.this).get(PickupDeliveryOrderViewModel.class);
        initView();
        handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
//        cashDoItems = intent.getParcelableArrayListExtra(AppConstants.CASH_DOITEM_KEY);
//        cashProducts = intent.getParcelableArrayListExtra(AppConstants.CASH_PRODUCT_KEY);
        List<BaseListItem> doItems = new ArrayList<>();
        doItems.addAll(cashDoItems);
        adapter.addItemsList(BaseListItem.setItemType(doItems, AppConstants.TYPE_CASH_SALES_ITEM), 1, cashProducts);
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.pickup_stock), R.drawable.ic_pickup_toolbar);
        setWareHouseDetails();
        initRecyclerView();
    }

    private void initRecyclerView() {
        expandableListView.setLayoutManager(new LinearLayoutManager(PickupConfirmationActivity.this));
        expandableListView.addItemDecoration(new DividerItemDecoration(PickupConfirmationActivity.this, DividerItemDecoration.VERTICAL));
        List<BaseListItem> listItems = new ArrayList<>();
        BaseListItem item = new BaseListItem();
        item.setOrdersHeader("Delivery Orders");
        item.setItemType(AppConstants.TYPE_ORDERS_HEADER);
        listItems.add(item);
        BaseListItem item2 = new BaseListItem();
        item2.setOrdersHeader("Case Sales");
        item2.setItemType(AppConstants.TYPE_ORDERS_HEADER);
        listItems.add(item2);
        adapter = new DoExpandableListAdapter(PickupConfirmationActivity.this, listItems);
        expandableListView.setAdapter(adapter);
    }

    private void setWareHouseDetails() {
        wareHouseInfoTextView.setText(pickupDeliveryOrderViewModel.getWareHouseNameAddress());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_pickup_confirmation);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
        }
    }
}
