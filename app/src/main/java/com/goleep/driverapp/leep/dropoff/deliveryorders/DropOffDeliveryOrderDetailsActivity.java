package com.goleep.driverapp.leep.dropoff.deliveryorders;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.OrderItemsListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customviews.CustomEditText;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ProductEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.LogUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.dropoff.deliveryorders.DropOffDeliveryOrderDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA;
import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_SERVER_DATE_FORMAT;

public class DropOffDeliveryOrderDetailsActivity extends ParentAppCompatActivity {

    private TextView tvCustomerName;
    private TextView tvStoreAddress;
    private TextView tvDoNumber;
    private TextView tvDate;
    private TextView tvSchedule;
    private TextView tvItemsCount;
    private Button btSkipPayment;
    private Button btCollectPayment;

    private LinearLayout updateQuantityLayout;
    private CustomEditText etUnits;
    private TextView tvProductName;
    private TextView invalidQuantityError;
    private Button btUpdate;
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
        viewModel = ViewModelProviders.of(this).get(DropOffDeliveryOrderDetailsViewModel.class);
        if (getIntent().getExtras() != null) {
            viewModel.setDeliveryOrderId(getIntent().getExtras().getInt(IntentConstants.DELIVERY_ORDER_ID));
        }
        connectUIElements();
        initialiseToolbar();
        initialiseRecyclerView();
        fetchDeliveryOrderData();
        fetchDeliveryOrderItems();
        updateDeliveryOrderUI();
        setCLickListenersOnButtons();
    }

    private void connectUIElements() {
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

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.delivery_orders), R.drawable.ic_drop_off_toolbar);
    }

    private void initialiseRecyclerView() {
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        orderItemsListAdapter = new OrderItemsListAdapter(new ArrayList<>());
        orderItemsListAdapter.setOrderItemClickEventListener(deliveryOrderItemEventListener);
        viewModel.deliveryOrderItems(viewModel.getDeliveryOrderId()).observe(this, orderItemEntities -> {
            if (orderItemEntities == null) return;
            orderItemsListAdapter.updateList(orderItemEntities);
            viewModel.setSelectedItemCount(0);
            for (OrderItemEntity entity : orderItemEntities) {
                if (entity.isSelected())
                    viewModel.setSelectedItemCount(viewModel.getSelectedItemCount() + 1);
            }
            llBottomButtons.setVisibility(viewModel.getSelectedItemCount() == 0 ? View.GONE : View.VISIBLE);
        });
        orderItemsRecyclerView.setAdapter(orderItemsListAdapter);
    }

    private void fetchDeliveryOrderData() {
        viewModel.deliveryOrder(viewModel.getDeliveryOrderId());
    }

    private void fetchDeliveryOrderItems() {
        showProgressDialog();
        viewModel.fetchDeliveryOrderItems(viewModel.getDeliveryOrderId(), orderItemNetworkCallBack);
    }

    private void updateDeliveryOrderUI() {
        DeliveryOrderEntity deliveryOrder = viewModel.getDeliveryOrder();
        if (deliveryOrder != null) {
            tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
            tvStoreAddress.setText(StringUtils.getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2()));
            tvDoNumber.setText(deliveryOrder.getDoNumber() == null ? "-" : deliveryOrder.getDoNumber());
            tvDate.setText(DateTimeUtils.convertdDate(deliveryOrder.getPreferredDeliveryDate(), ORDER_SERVER_DATE_FORMAT, ORDER_DISPLAY_DATE_FORMAT_COMMA));
            tvSchedule.setText(DateTimeUtils.timeDurationIn12HrFormat(deliveryOrder.getPreferredDeliveryTime()));
            int deliveryOrderCount = deliveryOrder.getDeliveryOrderItemsCount();
            tvItemsCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, deliveryOrderCount, deliveryOrderCount)));
        } else {
            LogUtils.error(this.getLocalClassName(), "--------Delivery order is null--------");
        }
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                AppUtils.hideKeyboard(this.getCurrentFocus());
                finish();
                break;

            case R.id.bt_update:
                onUpdateButtonTap();
                break;

        }
    }

    private UILevelNetworkCallback orderItemNetworkCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if (uiModels == null) {
                runOnUiThread(() -> dismissProgressDialog());
                if (toLogout) {
                    logoutUser();
                } else if (isDialogToBeShown) {
                    showNetworkRelatedDialogs(errorMessage);
                }
            } else if (uiModels.size() > 0) {
                viewModel.fetchBusinessLocation((Integer) uiModels.get(0), viewModel.getDeliveryOrder().getDestinationLocationId(), locationNetworkCallBack);
            }
        }
    };

    private UILevelNetworkCallback locationNetworkCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            runOnUiThread(() -> {
                dismissProgressDialog();
                if (uiModels == null) {
                    if (toLogout) {
                        logoutUser();
                    } else if (isDialogToBeShown) {
                        showNetworkRelatedDialogs(errorMessage);
                    }
                } else if (uiModels.size() > 0) {
                    Location location = (Location) uiModels.get(0);
                    viewModel.setBusinessAddress(StringUtils.getAddress(location));
                    viewModel.setOutstandingBalance(location.getOutstandingBalance());
                }
            });
        }
    };

    private void initialiseUpdateQuantityView() {
        etUnits.setKeyImeChangeListener(this::hideUpdateQuantityView);
        etUnits.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                hideUpdateQuantityView();
                AppUtils.hideKeyboard(etUnits);
            }
            return true;
        });
        etUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (viewModel.getSelectedOrderItem() != null) {
                    int maxUnits = viewModel.getSelectedOrderItem().getMaxQuantity();
                    String newUnitsText = etUnits.getText().toString();
                    if (newUnitsText.length() > 0) {
                        int newUnits = Integer.valueOf(newUnitsText);
                        boolean isValid = newUnits <= maxUnits && newUnits != 0;
                        invalidQuantityError.setVisibility(isValid ? View.INVISIBLE : View.VISIBLE);
                        btUpdate.setEnabled(isValid);
                    } else {
                        btUpdate.setEnabled(false);
                    }
                }
            }
        });

        btUpdate.setOnClickListener(this);
    }

    private void onUpdateButtonTap(){
        if (viewModel.getSelectedOrderItem() != null) {
            viewModel.updateOrderItemQuantity(viewModel.getSelectedOrderItem().getId(), Integer.valueOf(etUnits.getText().toString()));
            hideUpdateQuantityView();
            AppUtils.hideKeyboard(etUnits);
        }
    }

    private void displayUpdateQuantityView(int itemId, int currentUnits) {
        etUnits.requestFocus();
        OrderItemEntity selectedOrderItem = viewModel.orderItem(itemId);
        viewModel.setSelectedOrderItem(selectedOrderItem);
        if (selectedOrderItem != null && selectedOrderItem.getProduct() != null) {
            ProductEntity product = selectedOrderItem.getProduct();
            tvProductName.setText(product.getName() + " " + product.getWeight() + product.getWeightUnit());
            etUnits.setText("");
            etUnits.setHint(String.valueOf(currentUnits));
            updateQuantityLayout.setVisibility(View.VISIBLE);
            invalidQuantityError.setVisibility(View.INVISIBLE);
            btUpdate.setEnabled(false);
            AppUtils.showKeyboard(etUnits);
        }
    }

    private void hideUpdateQuantityView() {
        updateQuantityLayout.setVisibility(View.GONE);
        viewModel.setSelectedOrderItem(null);
    }

    private void setCLickListenersOnButtons() {
        btSkipPayment.setOnClickListener(v -> gotoPaymentConfirmationScreen());

        btCollectPayment.setOnClickListener(v -> gotoPaymentCollectScreen());
    }

    private void gotoPaymentConfirmationScreen() {
        Intent paymentConfirmationIntent = new Intent(DropOffDeliveryOrderDetailsActivity.this, DropOffPaymentConfirmationActivity.class);
        paymentConfirmationIntent.putExtra(IntentConstants.DELIVERY_ORDER_ID, viewModel.getDeliveryOrderId());
        paymentConfirmationIntent.putExtra(IntentConstants.BUSINESS_ADDRESS, viewModel.getBusinessAddress());
        paymentConfirmationIntent.putExtra(IntentConstants.CURRENT_SALE, viewModel.currentSales());
        paymentConfirmationIntent.putExtra(IntentConstants.OUTSTANDING_BALANCE, viewModel.getOutstandingBalance());
        paymentConfirmationIntent.putExtra(IntentConstants.PAYMENT_COLLECTED, 0.0);
        startActivity(paymentConfirmationIntent);
    }

    private void gotoPaymentCollectScreen() {
        Intent doCollectPaymentIntent = new Intent(DropOffDeliveryOrderDetailsActivity.this, DropOffPaymentCollectActivity.class);
        doCollectPaymentIntent.putExtra(IntentConstants.DELIVERY_ORDER_ID, viewModel.getDeliveryOrderId());
        doCollectPaymentIntent.putExtra(IntentConstants.BUSINESS_ADDRESS, viewModel.getBusinessAddress());
        doCollectPaymentIntent.putExtra(IntentConstants.CURRENT_SALE, viewModel.currentSales());
        doCollectPaymentIntent.putExtra(IntentConstants.OUTSTANDING_BALANCE, viewModel.getOutstandingBalance());
        startActivity(doCollectPaymentIntent);
    }
}
