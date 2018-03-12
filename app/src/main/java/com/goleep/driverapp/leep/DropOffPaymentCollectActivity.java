package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ProductEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.DropOffPaymentCollectViewModel;

import java.util.Locale;

public class DropOffPaymentCollectActivity extends ParentAppCompatActivity {

    private CustomTextView tvCustomerName;
    private CustomTextView tvStoreAddress;
    private CustomTextView tvDoNumber;
    private CustomTextView tvDate;
    private CustomTextView tvTime;
    private CustomTextView tvCurrentSales;
    private CustomTextView tvOutstandingBalance;
    private CustomTextView tvGrandTotal;
    private CustomTextView tvItemCount;
    private CustomEditText etPaymentCollected;
    private CustomButton btDone;
    private LinearLayout llItemListLayout;
    private LinearLayout llItemSummaryLayout;

    private DropOffPaymentCollectViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_drop_off_payment_collect);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(DropOffPaymentCollectViewModel.class);
        if (getIntent().getExtras() != null) {
            viewModel.setDeliveryOrderId(getIntent().getExtras().getInt(IntentConstants.DELIVERY_ORDER_ID));
        }
        connectUIElements();
        addListeners();
        initialiseToolbar();
        fetchDeliveryOrderData();
        fetchDeliveryOrderItems();
        updateDeliveryOrderUI();
        addItemViewsToLayout();
        updateSalesValues();
        fetchLocationDetails();
    }

    private void connectUIElements() {
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvStoreAddress = findViewById(R.id.tv_store_address);
        tvDoNumber = findViewById(R.id.tv_do_number);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        llItemListLayout = findViewById(R.id.ll_item_list_layout);
        llItemSummaryLayout = findViewById(R.id.ll_item_summary_layout);
        tvCurrentSales = findViewById(R.id.tv_current_sales);
        tvOutstandingBalance = findViewById(R.id.tv_outstanding_balance);
        tvGrandTotal = findViewById(R.id.tv_grand_total);
        tvItemCount = findViewById(R.id.tv_item_count);
        etPaymentCollected = findViewById(R.id.et_payment_collected);
        btDone = findViewById(R.id.bt_done);
    }

    private void addListeners() {
        llItemSummaryLayout.setOnClickListener(this);
        btDone.setOnClickListener(this);
        etPaymentCollected.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                btDone.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.delivery_orders), R.drawable.ic_drop_off_toolbar);
    }

    private void fetchDeliveryOrderData() {
        viewModel.setDeliveryOrder(viewModel.deliveryOrder(viewModel.getDeliveryOrderId()));
    }

    private void fetchDeliveryOrderItems() {
        viewModel.setOrderItems(viewModel.getSelectedOrderItems());
    }

    private void updateDeliveryOrderUI() {
        DeliveryOrderEntity deliveryOrder = viewModel.getDeliveryOrder();
        if (deliveryOrder != null) {
            tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
            tvStoreAddress.setText("");
            tvDoNumber.setText(deliveryOrder.getDoNumber() == null ? "-" : deliveryOrder.getDoNumber());
            tvDate.setText(viewModel.dateToDisplay(deliveryOrder.getPreferredDeliveryDate()));
            tvTime.setText(viewModel.currentTimeToDisplay());
        }
    }

    private void addItemViewsToLayout() {
        llItemListLayout.addView(listHeaderView());
        llItemListLayout.addView(dividerView());

        for (OrderItemEntity orderItem : viewModel.getOrderItems()) {
            llItemListLayout.addView(orderListItemView(orderItem));
            llItemListLayout.addView(dividerView());
        }

    }

    private View listHeaderView() {
        return LayoutInflater.from(this).inflate(R.layout.item_list_header_layout, llItemListLayout, false);
    }

    private View dividerView() {
        View dividerView = LayoutInflater.from(this).inflate(R.layout.divider_view, llItemListLayout, false);
        dividerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
        return dividerView;
    }

    private View orderListItemView(OrderItemEntity orderItem) {
        View orderItemView = LayoutInflater.from(this).inflate(R.layout.do_details_list_item, llItemListLayout, false);
        CustomTextView tvProductName = orderItemView.findViewById(R.id.product_name_text_view);
        CustomTextView tvProductQuantity = orderItemView.findViewById(R.id.quantity_text_view);
        CustomTextView tvAmount = orderItemView.findViewById(R.id.amount_text_view);
        CustomTextView tvUnits = orderItemView.findViewById(R.id.units_text_view);
        CheckBox productCheckbox = orderItemView.findViewById(R.id.product_checkbox);
        productCheckbox.setVisibility(View.GONE);

        ProductEntity product = orderItem.getProduct();
        if (product != null) {
            tvProductName.setText(product.getName() == null ? "" : product.getName());
        }
        tvProductQuantity.setText(getString(R.string.weight_with_units, product.getWeight(), product.getWeightUnit()));
        tvUnits.setText(String.valueOf(orderItem.getQuantity()));

        double value = orderItem.getQuantity() * orderItem.getPrice();
        tvAmount.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.format(Locale.getDefault(), "%.02f", value)));
        return orderItemView;
    }

    private void updateSalesValues() {
        tvItemCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, viewModel.getOrderItems().size(), viewModel.getOrderItems().size())));
        ((CustomTextView) findViewById(R.id.tv_collected_amount_currency)).setText(AppUtils.userCurrencySymbol());
        tvCurrentSales.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.currrentSales())));
        tvOutstandingBalance.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getOutstandingBalance())));
        double total = viewModel.currrentSales() + viewModel.getOutstandingBalance();
        tvGrandTotal.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(total)));
    }

    private void addOnEditListener() {

    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;

            case R.id.ll_item_summary_layout:
                llItemListLayout.setVisibility(llItemListLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ((ImageView) findViewById(R.id.iv_expandable_indicator)).setImageResource(llItemListLayout.getVisibility() == View.VISIBLE ? R.drawable.ic_expandable_indicator_close : R.drawable.ic_expandable_indicator_open);
                break;

            case R.id.bt_done:
                break;
        }
    }

    private void fetchLocationDetails() {
        DeliveryOrderEntity deliveryOrder = viewModel.getDeliveryOrder();
        if (deliveryOrder != null) {
            viewModel.fetchDestinationBusinessId(deliveryOrder.getSourceLocationId(), deliveryOrder.getDestinationLocationId(), businessIdCallBack);
        }

    }

    private UILevelNetworkCallback businessIdCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else {
                showNetworkRelatedDialogs(errorMessage);
            }
        } else {
            if (uiModels != null && uiModels.size() > 0) {
                Integer businessId = (Integer) uiModels.get(0);
                getDestinationBusinessLocation(businessId);
            }
        }
    };

    private void getDestinationBusinessLocation(Integer businessId) {
        DeliveryOrderEntity deliveryOrder = viewModel.getDeliveryOrder();
        if (deliveryOrder != null) {
            viewModel.fetchBusinessLocation(businessId, deliveryOrder.getDestinationLocationId(), locationCallBack);
        }

    }

    private UILevelNetworkCallback locationCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else {
                showNetworkRelatedDialogs(errorMessage);
            }
        } else {
            if (uiModels.size() > 0) {
                runOnUiThread(() -> {
                    Location location = (Location) uiModels.get(0);
                    viewModel.setBusinessLocation(location);
                    viewModel.setOutstandingBalance(location.getOutstandingBalance());
                    tvStoreAddress.setText(viewModel.getAddress(location));
                    updateSalesValues();
                });

            }
        }
    };

}
