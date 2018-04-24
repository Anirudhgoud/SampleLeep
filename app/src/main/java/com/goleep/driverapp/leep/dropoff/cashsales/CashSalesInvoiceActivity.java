package com.goleep.driverapp.leep.dropoff.cashsales;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.ListUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.dropoff.cashsales.CashSalesInvoiceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesInvoiceActivity extends ParentAppCompatActivity {

    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;
    @BindView(R.id.tv_store_address)
    TextView tvAddress;
    @BindView(R.id.tv_date)
    TextView tvCurrentDate;
    @BindView(R.id.tv_time)
    TextView tvCurrentTime;
    @BindView(R.id.tv_item_count)
    TextView tvItemCount;
    @BindView(R.id.tv_returned_item_count)
    TextView tvReturnedItemCount;
    @BindView(R.id.ll_returned_label)
    LinearLayout llReturnedLabel;
    @BindView(R.id.ll_item_list_layout)
    LinearLayout llItemListLayout;
    @BindView(R.id.bt_continue)
    Button btContinue;
    @BindView(R.id.ll_item_summary_layout)
    LinearLayout llItemSummaryLayout;

    @BindView(R.id.tv_returned)
    TextView tvReturned;
    @BindView(R.id.tv_current_sale)
    TextView tvCurrentSales;
    @BindView(R.id.tv_previous_balance)
    TextView tvPreviousBalance;
    @BindView(R.id.tv_grand_total)
    TextView tvGrandTotal;
    @BindView(R.id.et_payment_collected)
    EditText etPaymentCollected;

    private CashSalesInvoiceViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_cash_sales_invoice);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(CashSalesInvoiceViewModel.class);
        ButterKnife.bind(this);
        extractIntentData();
        initialiseToolbar();
        setClickListeners();
        updateTopLayoutUI();
        fetchLocationDetails();
        displayProductList();
        updateItemSummaryUI();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent == null) return;
        viewModel.setConsumerLocation(intent.getParcelableExtra(IntentConstants.CONSUMER_LOCATION));
        List<Product> selectedProducts = intent.getParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST);
        List<Product> returnedProducts = intent.getParcelableArrayListExtra(IntentConstants.RETURNED_PRODUCT_LIST);
        viewModel.setScannedProducts((ArrayList<Product>) new ListUtils().combinedList(selectedProducts, returnedProducts));
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.cash_sales), R.drawable.ic_cash_sales);
    }

    private void updateTopLayoutUI() {
        findViewById(R.id.ll_do_number).setVisibility(View.GONE);

        Customer customer = viewModel.getConsumerLocation();
        if (customer == null) return;
        tvCustomerName.setText(StringUtils.toString(customer.getName(), ""));
        tvCustomerName.setTextSize(24);
        tvAddress.setText(StringUtils.toString(customer.getArea(), ""));
        tvCurrentDate.setText(DateTimeUtils.currentDateToDisplay());
        tvCurrentTime.setText(DateTimeUtils.currentTimeToDisplay());
    }

    private void updateItemSummaryUI() {
        int selectedProductCount = viewModel.getSelectedProductCount();
        int returnedProductCount = viewModel.getReturnedProductCount();
        if (selectedProductCount > 0){
            tvItemCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, selectedProductCount, selectedProductCount)));
            tvItemCount.setVisibility(View.VISIBLE);
        }else {
            tvItemCount.setVisibility(View.GONE);
        }
        if (returnedProductCount > 0){
            tvReturnedItemCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, returnedProductCount, returnedProductCount)));
            tvReturnedItemCount.setVisibility(View.VISIBLE);
            llReturnedLabel.setVisibility(View.VISIBLE);
        }else {
            tvReturnedItemCount.setVisibility(View.GONE);
            llReturnedLabel.setVisibility(View.GONE);
        }
    }

    private void displayProductList() {
        List<Product> scannedProducts = viewModel.getScannedProducts();

        llItemListLayout.addView(listHeaderView());
        llItemListLayout.addView(dividerView());

        for (Product product : scannedProducts) {
            llItemListLayout.addView(productListItemView(product));
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

    private View productListItemView(Product product) {
        View orderItemView = LayoutInflater.from(this).inflate(R.layout.cash_sales_returns_list_item, llItemListLayout, false);
        if (product == null) return orderItemView;

        TextView tvProductName = orderItemView.findViewById(R.id.product_name_text_view);
        TextView tvProductQuantity = orderItemView.findViewById(R.id.quantity_text_view);
        TextView tvAmount = orderItemView.findViewById(R.id.tv_amount);
        TextView tvUnits = orderItemView.findViewById(R.id.tv_units);
        LinearLayout llReturnedLabel = orderItemView.findViewById(R.id.ll_returned_label);
        TextView tvReturnedAmount = orderItemView.findViewById(R.id.tv_returned_amount);
        TextView tvReturnedUnits = orderItemView.findViewById(R.id.tv_return_units);
        TextView tvReturnreason = orderItemView.findViewById(R.id.tv_return_reason);

        tvProductName.setText(StringUtils.toString(product.getProductName(), ""));
        tvProductQuantity.setText(getString(R.string.weight_with_units, product.getWeight(), product.getWeightUnit()));

        if (product.getQuantity() > 0){
            tvUnits.setText(String.valueOf(product.getQuantity()));
            tvAmount.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.format(Locale.getDefault(), "%.02f", product.getTotalPrice())));
            viewModel.incrementSelectedProductCount();
        }else {
            tvUnits.setVisibility(View.GONE);
            tvAmount.setVisibility(View.GONE);
        }

        if (product.getReturnQuantity() > 0){
            tvReturnedUnits.setText(String.valueOf(product.getReturnQuantity()));
            tvReturnedAmount.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.format(Locale.getDefault(), "%.02f", product.getTotalReturnsPrice())));
            llReturnedLabel.setVisibility(View.VISIBLE);
            tvReturnedAmount.setVisibility(View.VISIBLE);
            tvReturnreason.setVisibility(View.VISIBLE);
            ReturnReason returnReason = product.getReturnReason();
            if (returnReason != null && returnReason.getReason() != null) tvReturnreason.setText(returnReason.getReason());
            viewModel.incrementReturnedProductCount();
        }else {
            llReturnedLabel.setVisibility(View.GONE);
            tvReturnedAmount.setVisibility(View.GONE);
            tvReturnreason.setVisibility(View.GONE);
        }
        return orderItemView;
    }

    private void setClickListeners() {
        btContinue.setOnClickListener(this);
        llItemSummaryLayout.setOnClickListener(this);
    }

    private void fetchLocationDetails(){
        Customer consumer = viewModel.getConsumerLocation();
        if (consumer == null) return;
        showProgressDialog();
        viewModel.fetchBusinessLocation(consumer.getBusinessId(), consumer.getId(), locationNetworkCallback);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;

            case R.id.bt_continue:
                onContinueButtonTap();
                break;

            case R.id.ll_item_summary_layout:
                llItemListLayout.setVisibility(llItemListLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ((ImageView) findViewById(R.id.iv_expandable_indicator)).setImageResource(llItemListLayout.getVisibility() == View.VISIBLE ? R.drawable.ic_expandable_indicator_close : R.drawable.ic_expandable_indicator_open);
                break;
        }
    }

    private void onContinueButtonTap() {
        if (etPaymentCollected.getText().length() > 0) gotoNextActivity();
    }



    private void onLocationDetailsFetched(Location location){
        if (location == null) return;
        String address = StringUtils.getAddress(location, viewModel.getConsumerLocation());
        tvAddress.setText(address);
        viewModel.updateAreaInConsumerLocation(address);
        double outstandingBalance = location.getOutstandingBalance();
        viewModel.setOutstandingBalance(outstandingBalance);
        updateAmountDetails(outstandingBalance);
    }

    private void updateAmountDetails(double outstandingBalance){
        double totalReturns = viewModel.totalReturnsValue();
        double totalCurrentSales = viewModel.totalCurrentSales();
        tvReturned.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(totalReturns)));
        tvCurrentSales.setText(amountWithCurrencySymbol(totalCurrentSales));
        tvPreviousBalance.setText(amountWithCurrencySymbol(outstandingBalance));
        tvGrandTotal.setText(amountWithCurrencySymbol(viewModel.grandTotal(totalReturns, totalCurrentSales, outstandingBalance)));
    }

    public String amountWithCurrencySymbol(Object amount){
        return getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(amount));
    }

    UILevelNetworkCallback locationNetworkCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> runOnUiThread(() -> {
        dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else if (isDialogToBeShown){
                showNetworkRelatedDialogs(errorMessage);
            }
        } else if (uiModels.size() > 0) {
            btContinue.setVisibility(View.VISIBLE);
            Location location = (Location) uiModels.get(0);
            onLocationDetailsFetched(location);
        }
    });

    private void gotoNextActivity(){
        Double paymentCollected = Double.valueOf(etPaymentCollected.getText().toString());
        if (paymentCollected == null) return;
        Intent intent = new Intent(this, CashSalesPaymentMethodActivity.class);
        intent.putExtra(IntentConstants.PAYMENT_COLLECTED, paymentCollected);
        intent.putExtra(IntentConstants.PREVIOUS_BALANCE, viewModel.getOutstandingBalance());
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, viewModel.getConsumerLocation());
        intent.putParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST, (ArrayList<Product>) viewModel.getScannedProducts());
        startActivity(intent);
    }
}
