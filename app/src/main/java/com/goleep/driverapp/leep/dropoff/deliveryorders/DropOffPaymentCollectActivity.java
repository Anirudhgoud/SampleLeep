package com.goleep.driverapp.leep.dropoff.deliveryorders;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ProductEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.viewmodels.dropoff.deliveryorders.DropOffPaymentCollectViewModel;

import java.util.Locale;

public class DropOffPaymentCollectActivity extends ParentAppCompatActivity {

    private TextView tvCustomerName;
    private TextView tvStoreAddress;
    private TextView tvDoNumber;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvCurrentSales;
    private TextView tvOutstandingBalance;
    private TextView tvGrandTotal;
    private TextView tvItemCount;
    private EditText etPaymentCollected;
    private Button btDone;
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
        getIntentExtras();
        initialiseToolbar();
        connectUIElements();
        addListeners();
        fetchDeliveryOrderData();
        fetchDeliveryOrderItems();
        updateDeliveryOrderUI();
        addItemViewsToLayout();
        updateSalesValues();
    }

    private void getIntentExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            viewModel.setDeliveryOrderId(bundle.getInt(IntentConstants.DELIVERY_ORDER_ID));
            viewModel.setBusinessAddress(bundle.getString(IntentConstants.BUSINESS_ADDRESS));
            viewModel.setCurrentSales(bundle.getDouble(IntentConstants.CURRENT_SALE));
            viewModel.setOutstandingBalance(bundle.getDouble(IntentConstants.OUTSTANDING_BALANCE));
        }
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
            tvStoreAddress.setText(viewModel.getBusinessAddress());
            tvDoNumber.setText(deliveryOrder.getDoNumber() == null ? "-" : deliveryOrder.getDoNumber());
            tvDate.setText(DateTimeUtils.currentDateToDisplay());
            tvTime.setText(DateTimeUtils.currentTimeToDisplay());
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
        TextView tvProductName = orderItemView.findViewById(R.id.product_name_text_view);
        TextView tvProductQuantity = orderItemView.findViewById(R.id.quantity_text_view);
        TextView tvAmount = orderItemView.findViewById(R.id.amount_text_view);
        TextView tvUnits = orderItemView.findViewById(R.id.units_text_view);
        CheckBox productCheckbox = orderItemView.findViewById(R.id.product_checkbox);
        productCheckbox.setVisibility(View.GONE);

        ProductEntity product = orderItem.getProduct();
        if (product != null) {
            tvProductName.setText(product.getName() == null ? "" : product.getName());
        }
        tvProductQuantity.setText(getString(R.string.weight_with_units, product.getWeight(), product.getWeightUnit()));
        tvUnits.setText(String.valueOf(orderItem.getQuantity()));

        double value = orderItem.getQuantity() * orderItem.getPrice();
        tvAmount.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.format(Locale.getDefault(), "%.02f", value)));
        return orderItemView;
    }

    private void updateSalesValues() {
        tvItemCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, viewModel.getOrderItems().size(), viewModel.getOrderItems().size())));
        ((TextView) findViewById(R.id.tv_collected_amount_currency)).setText(AppUtils.userCurrencySymbol(this));
        tvCurrentSales.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(viewModel.getCurrentSales())));
        tvOutstandingBalance.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(viewModel.getOutstandingBalance())));
        double total = viewModel.getCurrentSales() + viewModel.getOutstandingBalance();
        tvGrandTotal.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(total)));
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
                gotoPaymentMethodScreen();
                break;
        }
    }

    private void gotoPaymentMethodScreen() {
        Intent paymentMethodIntent = new Intent(DropOffPaymentCollectActivity.this, DropOffCollectPaymentMethodActivity.class);
        paymentMethodIntent.putExtra(IntentConstants.DELIVERY_ORDER_ID, viewModel.getDeliveryOrderId());
        paymentMethodIntent.putExtra(IntentConstants.BUSINESS_ADDRESS, viewModel.getBusinessAddress());
        paymentMethodIntent.putExtra(IntentConstants.CURRENT_SALE, viewModel.getCurrentSales());
        paymentMethodIntent.putExtra(IntentConstants.OUTSTANDING_BALANCE, viewModel.getOutstandingBalance());
        paymentMethodIntent.putExtra(IntentConstants.PAYMENT_COLLECTED, Double.valueOf(etPaymentCollected.getText().toString()));
        startActivity(paymentMethodIntent);
    }
}
