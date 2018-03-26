package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.ProductListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.ReturnOrderItem;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.HistoryDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryDetailsActivity extends ParentAppCompatActivity {

    @BindView(R.id.tv_customer_name)
    CustomTextView customerNameTv;
    @BindView(R.id.tv_store_address)
    CustomTextView storeAddressTv;
    @BindView(R.id.tv_do_number)
    CustomTextView doNumberTv;
    @BindView(R.id.tv_date)
    CustomTextView dateTv;
    @BindView(R.id.tv_schedule)
    CustomTextView timeTv;
    @BindView(R.id.tv_items_value)
    CustomTextView itemsTv;
    @BindView(R.id.tv_do_amount)
    CustomTextView doAmountTv;
    @BindView(R.id.order_items_recycler_view)
    RecyclerView productsList;

    private HistoryDetailsViewModel historyDetailsViewModel;
    private ProductListAdapter adapter;

    private UILevelNetworkCallback orderItemsCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if(uiModels != null){
                if(uiModels.size() > 0)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(uiModels.get(0) instanceof OrderItemEntity)
                            adapter.updateList((List<OrderItemEntity>)uiModels);
                        else if(uiModels.get(0) instanceof ReturnOrderEntity)
                            adapter.updateReturnOrdersList((List<ReturnOrderItem>)uiModels);
                    }
                });
            }
        }
    };

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(HistoryDetailsActivity.this);
        historyDetailsViewModel = ViewModelProviders.of(HistoryDetailsActivity.this).get(HistoryDetailsViewModel.class);
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_orders_history_details);
    }

    private void initView(){
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.history), R.drawable.ic_history_title_icon);
        initRecyclerView();
        processIntent();
    }

    private void initRecyclerView() {
        productsList.setLayoutManager(new LinearLayoutManager(HistoryDetailsActivity.this));
        productsList.addItemDecoration(new DividerItemDecoration(HistoryDetailsActivity.this,
                DividerItemDecoration.VERTICAL));
        adapter = new ProductListAdapter(new ArrayList<>());
        productsList.setAdapter(adapter);
    }

    private void processIntent(){
        String orderIdStr = getIntent().getStringExtra(IntentConstants.ORDER_ID);
        if(orderIdStr != null){
            String[] orderIdInfo = orderIdStr.split("#");
            int orderId = Integer.parseInt(orderIdInfo[1]);
            String orderType = orderIdInfo[0];
            if(orderType.equals("type_do")) {
                populateDoInfo(historyDetailsViewModel.getDeliveryOrderEntity(orderId));
                historyDetailsViewModel.fetchDoItems(orderId, orderItemsCallBack);
            } else {
                populateRoInfo(historyDetailsViewModel.getReturnOrderEntity(orderId));
                historyDetailsViewModel.fetchRoItems(orderId, orderItemsCallBack);
            }

        }
    }

    private void populateDoInfo(DeliveryOrderEntity deliveryOrderEntity) {
        if(deliveryOrderEntity != null){
            customerNameTv.setText(deliveryOrderEntity.getCustomerName());
            storeAddressTv.setText(StringUtils.getAddress(deliveryOrderEntity.getDestinationAddressLine1(),
                    deliveryOrderEntity.getDestinationAddressLine2()));
            doNumberTv.setText(deliveryOrderEntity.getDoNumber());
            dateTv.setText(DateTimeUtils.convertdDate(deliveryOrderEntity.getActualDeliveryDate(),
                    "yyyy-MM-dd", "dd MMM yyyy"));
            timeTv.setText(StringUtils.timeToDisplay(deliveryOrderEntity.getPreferredDeliveryTime()));
            itemsTv.setText(String.valueOf(deliveryOrderEntity.getDeliveryOrderItemsCount()));
            doAmountTv.setText(StringUtils.amountToDisplay(deliveryOrderEntity.getTotalValue()));
        }
    }

    private void populateRoInfo(ReturnOrderEntity returnOrderEntity){
        if(returnOrderEntity != null){
            customerNameTv.setText(returnOrderEntity.getCustomerName());
            storeAddressTv.setText(StringUtils.getAddress(returnOrderEntity.getSourceAddressLine1(),
                    returnOrderEntity.getSourceAddressLine2()));
            doNumberTv.setText(String.valueOf(returnOrderEntity.getRoNumber()));
            //dateTv.setText(DateTimeUtils.convertdDate(returnOrderEntity.getActualDeliveryDate(),
                   // "yyyy-MM-dd", "dd MMM yyyy"));
            //timeTv.setText(StringUtils.timeToDisplay(deliveryOrderEntity.getPreferredDeliveryTime()));
            itemsTv.setText(String.valueOf(returnOrderEntity.getReturnOrderItemsCount()));
            doAmountTv.setText(StringUtils.amountToDisplay((float) returnOrderEntity.getTotalValue()));
        }
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
        }
    }
}
