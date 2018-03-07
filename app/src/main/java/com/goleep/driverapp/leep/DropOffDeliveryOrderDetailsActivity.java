package com.goleep.driverapp.leep;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.OrderItemsListAdapter;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.utils.LogUtils;
import com.goleep.driverapp.viewmodels.DropOffDeliveryOrderDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class DropOffDeliveryOrderDetailsActivity extends ParentAppCompatActivity {

    private CustomTextView tvCustomerName;
    private CustomTextView tvStoreAddress;
    private CustomTextView tvDoNumber;
    private CustomTextView tvDate;
    private CustomTextView tvSchedule;
    private CustomTextView tvItemsCount;

    private DropOffDeliveryOrderDetailsViewModel viewModel;
    private RecyclerView orderItemsRecyclerView;
    private OrderItemsListAdapter orderItemsListAdapter;
    private int deliveryOrderId;
    private DeliveryOrderEntity deliveryOrder;

    private DeliveryOrderItemEventListener deliveryOrderItemEventListener = new DeliveryOrderItemEventListener() {
        @Override
        public void onUnitsTap(int itemId, int currentUnits) {

        }

        @Override
        public void onCheckboxTap(int itemId, boolean isChecked) {

        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        deliveryOrderId = getIntent().getExtras().getInt("delivery_order_id");
        super.setResources(R.layout.activity_droff_off_delivery_order_details);
    }

    @Override
    public void doInitialSetup() {
        connectUIElements();
        viewModel = ViewModelProviders.of(this).get(DropOffDeliveryOrderDetailsViewModel.class);
        initialiseToolbar();
        initialiseRecyclerView();
        fetchDeliveryOrderData();
        fetchDeliveriOrderItems();
        updateDeliveryOrderUI();
    }

    private void connectUIElements(){
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvStoreAddress = findViewById(R.id.tv_store_address);
        tvDoNumber = findViewById(R.id.tv_do_number);
        tvDate = findViewById(R.id.tv_date);
        tvSchedule = findViewById(R.id.tv_schedule);
        tvItemsCount = findViewById(R.id.tv_item_count);
        orderItemsRecyclerView = findViewById(R.id.order_items_recyclerview);
    }

    private void initialiseToolbar(){
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.delivery_orders), R.drawable.ic_drop_off_toolbar);
    }

    private void initialiseRecyclerView(){
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        orderItemsListAdapter = new OrderItemsListAdapter(new ArrayList<OrderItemEntity>());
        orderItemsListAdapter.setOrderItemClickEventListener(deliveryOrderItemEventListener);
        viewModel.deliveryOrderItems(deliveryOrderId).observe(this, new Observer<List<OrderItemEntity>>() {
            @Override
            public void onChanged(@Nullable List<OrderItemEntity> orderItemEntities) {
                orderItemsListAdapter.updateList(orderItemEntities);
            }
        });
        orderItemsRecyclerView.setAdapter(orderItemsListAdapter);
    }

    private void fetchDeliveryOrderData(){
        deliveryOrder = viewModel.deliveryOrder(deliveryOrderId);
    }

    private void fetchDeliveriOrderItems(){
        viewModel.fetchDeliveryOrderItems(deliveryOrderId, orderItemNetworkCallBack);
    }

    private void updateDeliveryOrderUI(){
        if(deliveryOrder != null){
            tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
            tvStoreAddress.setText(viewModel.getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2()));
            tvDoNumber.setText(deliveryOrder.getDoNumber() ==  null ? "-" : deliveryOrder.getDoNumber());
            tvDate.setText(viewModel.dateToDisplay(deliveryOrder.getPreferredDeliveryDate()));
            tvSchedule.setText(viewModel.timeToDisplay(deliveryOrder.getPreferredDeliveryTime()));
            tvItemsCount.setText(String.valueOf(deliveryOrder.getDeliveryOrderItemsCount()));
        }else {
            LogUtils.error(this.getLocalClassName(), "--------Delivery order is null--------");
        }

    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                finish();
                break;
        }
    }

    private UILevelNetworkCallback orderItemNetworkCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if(uiModels == null){
                if(toLogout){
                    logoutUser();
                }else {
                    showNetworkRelatedDialogs(errorMessage);
                }
            }
        }
    };
}
