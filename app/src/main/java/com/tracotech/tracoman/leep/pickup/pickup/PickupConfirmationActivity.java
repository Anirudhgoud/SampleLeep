package com.tracotech.tracoman.leep.pickup.pickup;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.DoExpandableListAdapter;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.helpers.customviews.LeepSuccessDialog;
import com.tracotech.tracoman.helpers.uimodels.BaseListItem;
import com.tracotech.tracoman.helpers.uimodels.CashSalesInfo;
import com.tracotech.tracoman.interfaces.SuccessDialogEventListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.leep.main.HomeActivity;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.utils.LogUtils;
import com.tracotech.tracoman.viewmodels.pickup.pickup.PickupDeliveryOrderViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickupConfirmationActivity extends ParentAppCompatActivity {
    @BindView(R.id.warehouse_info_text_view)
    TextView wareHouseInfoTextView;
    @BindView(R.id.expandable_list)
    RecyclerView expandableListView;
    @BindView(R.id.confirm_button)
    Button confirmButton;
    @BindView(R.id.map_button)
    LinearLayout mapButton;
    private DoExpandableListAdapter adapter;
    private PickupDeliveryOrderViewModel pickupDeliveryOrderViewModel;


    private UILevelNetworkCallback pickupConfirmCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage,
                                       boolean toLogout) {
            runOnUiThread(() -> {
                dismissProgressDialog();
                if (uiModels == null) {
                    if (toLogout) {
                        logoutUser();
                    } else if (isDialogToBeShown){
                        showNetworkRelatedDialogs(errorMessage);
                    }

                } else{
                    pickupDeliveryOrderViewModel.deleteDeliveryOrders(pickupDeliveryOrderViewModel.
                            getSelectedDeliveryOrders(), pickupDeliveryOrderViewModel.getCashSalesItems());
                    sendSuccessBroadcast();
                    showSuccessDialog();
                }
            });
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
        ArrayList<Integer> ids = intent.getIntegerArrayListExtra(AppConstants.CASH_DOITEM_KEY);
        pickupDeliveryOrderViewModel.setCashDoItems(intent.getIntegerArrayListExtra(AppConstants.CASH_DOITEM_KEY));
        pickupDeliveryOrderViewModel.setSelectedDeliveryOrders(intent.getIntegerArrayListExtra(AppConstants.DO_IDS_KEY));
        int locationId = intent.getIntExtra(IntentConstants.WAREHOUSE_ID, -1);
        if(locationId != -1)
            pickupDeliveryOrderViewModel.setWarehouse(locationId);
    }

    private void initView() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.pickup_stock), R.drawable.ic_pickup_toolbar);
        setWareHouseDetails();
        initRecyclerView();
        mapButton.setVisibility(View.GONE);
        confirmButton.setOnClickListener(this);
    }

    private void initRecyclerView() {
        expandableListView.setLayoutManager(new LinearLayoutManager(PickupConfirmationActivity.this));
        expandableListView.addItemDecoration(new DividerItemDecoration(PickupConfirmationActivity.this,
                DividerItemDecoration.VERTICAL));
        adapter = new DoExpandableListAdapter(PickupConfirmationActivity.this, new ArrayList<>());
        expandableListView.setAdapter(adapter);
        adapter.addCombinedListItems(generateAdapterItemList(pickupDeliveryOrderViewModel.getCashDoItems(),
                pickupDeliveryOrderViewModel.getSelectedDeliveryOrders()));
    }

    private List<BaseListItem> generateAdapterItemList(ArrayList<Integer> cashDoItems,
                                                       ArrayList<Integer> selectedDeliveryOrders) {
        List<BaseListItem> baseListItems = new ArrayList<>();

        List<DeliveryOrderEntity> deliveryOrderEntities = new ArrayList<>();
        for (int doId : selectedDeliveryOrders) {
            deliveryOrderEntities.add(pickupDeliveryOrderViewModel.getDeliveryOrder(doId));
        }
        BaseListItem deliveryOrderHeader = new BaseListItem();
        deliveryOrderHeader.setOrdersHeader(String.format(getString(R.string.do_header_label), deliveryOrderEntities.size()));
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
            if(csOrderItem != null) {
                csOrderItem.setItemType(AppConstants.TYPE_CASH_SALES_ITEM);
                pickupDeliveryOrderViewModel.getCashSalesItems().add(csOrderItem);
                totalValue += csOrderItem.getQuantity() * csOrderItem.getPrice();
            }
        }
        BaseListItem cashSalesHeader = new BaseListItem();
        cashSalesHeader.setOrdersHeader(getString(R.string.cash_sales));
        cashSalesHeader.setItemType(AppConstants.TYPE_ORDERS_HEADER);
        baseListItems.add(cashSalesHeader);
        BaseListItem cashSalesInfo = new CashSalesInfo(pickupDeliveryOrderViewModel.getCashSalesItems().size(), totalValue);
        cashSalesInfo.setItemType(AppConstants.TYPE_SALES_INFO);
        baseListItems.add(cashSalesInfo);
        BaseListItem itemsHeader = new BaseListItem();
        itemsHeader.setItemType(AppConstants.TYPE_ITEMS_HEADER);
        baseListItems.add(itemsHeader);
        baseListItems.addAll(pickupDeliveryOrderViewModel.getCashSalesItems());
        return baseListItems;
    }

    private void setWareHouseDetails() {
        wareHouseInfoTextView.setText(pickupDeliveryOrderViewModel.getWareHouseNameAddress());
    }

    private void showSuccessDialog() {
        LeepSuccessDialog successDialog = new LeepSuccessDialog(this, getString(R.string.pickup_success));
        successDialog.show();
        successDialog.setSuccessDialogEventListener(new SuccessDialogEventListener() {
            @Override
            public void onOkButtonTap() {
                goToHomeActivity();
            }

            @Override
            public void onCloseButtonTap() {
                goToHomeActivity();
            }

            @Override
            public void onPrintButtonTap() {
                LogUtils.debug(this.getClass().getSimpleName(), "Print tapped");
            }
        });
        successDialog.setPrintButtonVisibility(false);
    }

    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentConstants.PICKUP_SUCCESS, true);
        intent.putExtra(IntentConstants.TASK_SUCCESSFUL, true);
        startActivity(intent);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
            case R.id.confirm_button:
                performConfirm();
                break;
        }
    }

    private void performConfirm() {
        showProgressDialog();
        pickupDeliveryOrderViewModel.confirmPickup(pickupDeliveryOrderViewModel.getCashSalesItems(),
                pickupDeliveryOrderViewModel.getSelectedDeliveryOrders(), pickupConfirmCallBack);
    }

    private void sendSuccessBroadcast(){
        Intent intent = new Intent(IntentConstants.TASK_SUCCESSFUL);
        intent.putExtra(IntentConstants.TASK_SUCCESSFUL, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
