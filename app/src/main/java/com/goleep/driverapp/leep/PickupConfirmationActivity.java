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
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.helpers.uimodels.CashSalesInfo;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
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
    @BindView(R.id.confirm_button)
    CustomButton confirmButton;
    private DoExpandableListAdapter adapter;
    private PickupDeliveryOrderViewModel pickupDeliveryOrderViewModel;
    private ArrayList<Integer> cashDoItems = new ArrayList<>();
    private ArrayList<Integer> selectedDeliveryOrders = new ArrayList<>();
    private List<OrderItemEntity> cashSalesItems = new ArrayList<>();

    private UILevelNetworkCallback pickupConfirmCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage,
                                       boolean toLogout) {
            if (!isDialogToBeShown && errorMessage == null && !toLogout) {
                pickupDeliveryOrderViewModel.deleteDeliveryOrders(selectedDeliveryOrders, cashSalesItems);
                showSuccessDialog(getString(R.string.pickup_success));
            }
        }
    };

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        pickupDeliveryOrderViewModel = ViewModelProviders.of(
                PickupConfirmationActivity.this).get(PickupDeliveryOrderViewModel.class);
        handleIntent();
        initView();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_pickup_confirmation);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        cashDoItems = intent.getIntegerArrayListExtra(AppConstants.CASH_DOITEM_KEY);
        selectedDeliveryOrders = intent.getIntegerArrayListExtra(AppConstants.DO_IDS_KEY);
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.pickup_stock), R.drawable.ic_pickup_toolbar);
        setWareHouseDetails();
        initRecyclerView();
        confirmButton.setOnClickListener(this);
    }

    private void initRecyclerView() {
        expandableListView.setLayoutManager(new LinearLayoutManager(PickupConfirmationActivity.this));
        expandableListView.addItemDecoration(new DividerItemDecoration(PickupConfirmationActivity.this,
                DividerItemDecoration.VERTICAL));
        adapter = new DoExpandableListAdapter(PickupConfirmationActivity.this, new ArrayList<BaseListItem>());
        expandableListView.setAdapter(adapter);
        adapter.addCombinedListItems(generateAdapterItemList(cashDoItems, selectedDeliveryOrders));
    }

    private List<BaseListItem> generateAdapterItemList(ArrayList<Integer> cashDoItems,
                                                       ArrayList<Integer> selectedDeliveryOrders) {
        List<BaseListItem> baseListItems = new ArrayList<>();

        List<DeliveryOrderEntity> deliveryOrderEntities = new ArrayList<>();
        for (int doId : selectedDeliveryOrders) {
            deliveryOrderEntities.add(pickupDeliveryOrderViewModel.getDeliveryOrder(doId));
        }
        BaseListItem deliveryOrderHeader = new BaseListItem();
        deliveryOrderHeader.setOrdersHeader(String.format(getString(R.string.do_header_label),
                deliveryOrderEntities.size()));
        deliveryOrderHeader.setItemType(AppConstants.TYPE_ORDERS_HEADER);
        baseListItems.add(deliveryOrderHeader);
        for (DeliveryOrderEntity deliveryOrderEntity : deliveryOrderEntities) {
            deliveryOrderEntity.setItemType(AppConstants.TYPE_HEADER);
            baseListItems.add(deliveryOrderEntity);

            List<OrderItemEntity> orderItemEntities = pickupDeliveryOrderViewModel.
                    getOrderItemsList(deliveryOrderEntity.getId());
            BaseListItem itemsHeader = new BaseListItem();
            itemsHeader.setItemType(AppConstants.TYPE_ITEMS_HEADER);
            baseListItems.add(itemsHeader);
            for (OrderItemEntity orderItemEntity : orderItemEntities) {
                orderItemEntity.setItemType(AppConstants.TYPE_DO_ITEM);
                baseListItems.add(orderItemEntity);
            }
        }
        int totalValue = 0;
        for (int cashSalesId : cashDoItems) {
            OrderItemEntity csOrderItem = pickupDeliveryOrderViewModel.getDeliveryOrderItem(cashSalesId);
            csOrderItem.setItemType(AppConstants.TYPE_CASH_SALES_ITEM);
            cashSalesItems.add(csOrderItem);
            totalValue += csOrderItem.getQuantity() * csOrderItem.getPrice();
        }
        BaseListItem cashSalesHeader = new BaseListItem();
        cashSalesHeader.setOrdersHeader(getString(R.string.cash_sales));
        cashSalesHeader.setItemType(AppConstants.TYPE_ORDERS_HEADER);
        baseListItems.add(cashSalesHeader);
        BaseListItem cashSalesInfo = new CashSalesInfo(cashSalesItems.size(), totalValue);
        cashSalesInfo.setItemType(AppConstants.TYPE_SALES_INFO);
        baseListItems.add(cashSalesInfo);
        BaseListItem itemsHeader = new BaseListItem();
        itemsHeader.setItemType(AppConstants.TYPE_ITEMS_HEADER);
        baseListItems.add(itemsHeader);
        baseListItems.addAll(cashSalesItems);
        return baseListItems;
    }

    private void setWareHouseDetails() {
        wareHouseInfoTextView.setText(pickupDeliveryOrderViewModel.getWareHouseNameAddress());
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
            case R.id.confirm_button:
                pickupDeliveryOrderViewModel.confirmPickup(cashSalesItems, selectedDeliveryOrders, pickupConfirmCallBack);
                break;
        }
    }
}
