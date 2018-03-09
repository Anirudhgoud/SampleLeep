package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.OrderItemsListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ProductEntity;
import com.goleep.driverapp.utils.AppUtils;
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
    private CustomButton btSkipPayment;
    private CustomButton btCollectPayment;

    private LinearLayout updateQuantityLayout;
    private CustomEditText etUnits;
    private CustomTextView tvProductName;
    private CustomTextView invalidQuantityError;
    private CustomButton btUpdate;
    private LinearLayout llBottomButtons;

    private DropOffDeliveryOrderDetailsViewModel viewModel;
    private RecyclerView orderItemsRecyclerView;
    private OrderItemsListAdapter orderItemsListAdapter;

    private DeliveryOrderItemEventListener deliveryOrderItemEventListener = new DeliveryOrderItemEventListener() {
        @Override
        public void onUnitsTap(int itemId, int maxUnits) {
            displayUpdateQuantityView(itemId, maxUnits);
        }

        @Override
        public void onCheckboxTap(int itemId, boolean isChecked) {
            viewModel.updateOrderItemSelectionStatus(itemId, isChecked);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_droff_off_delivery_order_details);
    }

    @Override
    public void doInitialSetup() {
        connectUIElements();
        viewModel = ViewModelProviders.of(this).get(DropOffDeliveryOrderDetailsViewModel.class);
        if (getIntent().getExtras() != null) {
            viewModel.setDeliveryOrderId(getIntent().getExtras().getInt(IntentConstants.DELIVERY_ORDER_ID));
        }
        initialiseToolbar();
        initialiseRecyclerView();
        fetchDeliveryOrderData();
        fetchDeliveryOrderItems();
        updateDeliveryOrderUI();
        setCLickListenersOnButtons();
    }

    private void connectUIElements(){
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvCustomerName.requestFocus();
        tvStoreAddress = findViewById(R.id.tv_store_address);
        tvDoNumber = findViewById(R.id.tv_do_number);
        tvDate = findViewById(R.id.tv_date);
        tvSchedule = findViewById(R.id.tv_schedule);
        tvItemsCount = findViewById(R.id.tv_item_count);
        orderItemsRecyclerView = findViewById(R.id.order_items_recyclerview);
        btSkipPayment = findViewById(R.id.bt_skip_payment);
        btCollectPayment = findViewById(R.id.bt_collect_payment);

        updateQuantityLayout = findViewById(R.id.update_quantity_view);
        tvProductName = findViewById(R.id.product_name_text_view);
        etUnits = findViewById(R.id.et_units);
        invalidQuantityError = findViewById(R.id.invalid_quantity_error);
        btUpdate = findViewById(R.id.bt_update);
        llBottomButtons = findViewById(R.id.ll_bottom_buttons);
        initialiseUpdateQuantityView();
    }

    private void initialiseToolbar(){
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.delivery_orders), R.drawable.ic_drop_off_toolbar);
    }

    private void initialiseRecyclerView(){
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        orderItemsListAdapter = new OrderItemsListAdapter(new ArrayList<>());
        orderItemsListAdapter.setOrderItemClickEventListener(deliveryOrderItemEventListener);
        viewModel.deliveryOrderItems(viewModel.getDeliveryOrderId()).observe(this, orderItemEntities -> {
            orderItemsListAdapter.updateList(orderItemEntities);
            viewModel.setSelectedItemCount(0);
            for (OrderItemEntity entity : orderItemEntities){
                if (entity.isSelected())
                    viewModel.setSelectedItemCount(viewModel.getSelectedItemCount() + 1);
            }
            LogUtils.error(this.getLocalClassName(), "-----SelectedCount" + viewModel.getSelectedItemCount());
            llBottomButtons.setVisibility(viewModel.getSelectedItemCount() == 0 ? View.GONE : View.VISIBLE);
        });
        orderItemsRecyclerView.setAdapter(orderItemsListAdapter);
    }

    private void fetchDeliveryOrderData(){
        viewModel.deliveryOrder(viewModel.getDeliveryOrderId());
    }

    private void fetchDeliveryOrderItems(){
        viewModel.fetchDeliveryOrderItems(viewModel.getDeliveryOrderId(), orderItemNetworkCallBack);
    }

    private void updateDeliveryOrderUI(){
        DeliveryOrderEntity deliveryOrder = viewModel.getDeliveryOrder();
        if(deliveryOrder != null){
            tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
            tvStoreAddress.setText(viewModel.getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2()));
            tvDoNumber.setText(deliveryOrder.getDoNumber() ==  null ? "-" : deliveryOrder.getDoNumber());
            tvDate.setText(viewModel.dateToDisplay(deliveryOrder.getPreferredDeliveryDate()));
            tvSchedule.setText(viewModel.timeToDisplay(deliveryOrder.getPreferredDeliveryTime()));
            int deliveryOrderCount = deliveryOrder.getDeliveryOrderItemsCount();
            tvItemsCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, deliveryOrderCount, deliveryOrderCount)));
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
                }else if(isDialogToBeShown) {
                    showNetworkRelatedDialogs(errorMessage);
                }
            }
        }
    };

    private void initialiseUpdateQuantityView(){
        etUnits.setKeyImeChangeListener((keyCode, event) -> hideUpdateQuantityView());
        etUnits.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                hideUpdateQuantityView();
                AppUtils.toggleKeyboard(etUnits, getApplicationContext());
            }
            return true;
        });
        etUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (viewModel.getSelectedOrderItem() != null) {
                    int maxUnits = viewModel.getSelectedOrderItem().getMaxQuantity();
                    String newUnitsText = etUnits.getText().toString();
                    if(newUnitsText.length() > 0){
                        int newUnits = Integer.valueOf(newUnitsText);
                        boolean isValid = newUnits <= maxUnits && newUnits != 0;
                        invalidQuantityError.setVisibility(isValid ? View.INVISIBLE : View.VISIBLE);
                        btUpdate.setEnabled(isValid);
                    }else{
                        btUpdate.setEnabled(false);
                    }
                }
            }
        });

        btUpdate.setOnClickListener(v -> {
            if (viewModel.getSelectedOrderItem() != null) {
                viewModel.updateOrderItemQuantity(viewModel.getSelectedOrderItem().getId(), Integer.valueOf(etUnits.getText().toString()));
                hideUpdateQuantityView();
                AppUtils.toggleKeyboard(etUnits, getApplicationContext());
            }
        });
    }

    private void displayUpdateQuantityView(int itemId, int currentUnits){
        etUnits.requestFocus();
        OrderItemEntity selectedOrderItem = viewModel.orderItem(itemId);
        viewModel.setSelectedOrderItem(selectedOrderItem);
        if(selectedOrderItem != null && selectedOrderItem.getProduct() != null){
            ProductEntity product = selectedOrderItem.getProduct();
            tvProductName.setText(product.getName() + " " + product.getWeight() + product.getWeightUnit());
            etUnits.setText("");
            etUnits.setHint(String.valueOf(currentUnits));
            updateQuantityLayout.setVisibility(View.VISIBLE);
            invalidQuantityError.setVisibility(View.INVISIBLE);
            btUpdate.setEnabled(false);
            AppUtils.toggleKeyboard(etUnits, getApplicationContext());
        }
    }

    private void hideUpdateQuantityView(){
        updateQuantityLayout.setVisibility(View.GONE);
        viewModel.setSelectedOrderItem(null);
    }

    private void setCLickListenersOnButtons() {
        btSkipPayment.setOnClickListener(v -> {

        });

        btCollectPayment.setOnClickListener(v -> {

        });
    }
}
