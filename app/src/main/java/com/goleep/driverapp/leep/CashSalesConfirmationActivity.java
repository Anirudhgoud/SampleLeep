package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.CashSalesConfirmationViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesConfirmationActivity extends ParentAppCompatActivity {

    @BindView(R.id.tv_customer_name)
    CustomTextView tvCustomerName;
    @BindView(R.id.tv_store_address)
    CustomTextView tvAddress;
    @BindView(R.id.tv_date)
    CustomTextView tvCurrentDate;
    @BindView(R.id.tv_time)
    CustomTextView tvCurrentTime;
    @BindView(R.id.tv_item_count)
    CustomTextView tvItemCount;
    @BindView(R.id.ll_item_list_layout)
    LinearLayout llItemListLayout;
    @BindView(R.id.bt_take_returns)
    Button btTakeReturns;
    @BindView(R.id.bt_skip_payment)
    Button btSkipPayment;
    @BindView(R.id.bt_collect_payment)
    Button btCollectPayment;

    private CashSalesConfirmationViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_cash_sales_confirmation);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(CashSalesConfirmationViewModel.class);
        ButterKnife.bind(this);
        extractIntentData();
        initialiseToolbar();
        setClickListeners();
        updateTopLayoutUI();
        updateItemSummaryUI();
        displayProductList();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent == null) return;
        viewModel.setConsumerLocation(intent.getParcelableExtra(IntentConstants.CONSUMER_LOCATION));
        viewModel.setScannedProducts(intent.getParcelableArrayListExtra(IntentConstants.PRODUCT_LIST));
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
        tvCurrentDate.setText(viewModel.currentDateToDisplay());
        tvCurrentTime.setText(viewModel.currentTimeToDisplay());
    }

    private void updateItemSummaryUI() {
        int productCount = viewModel.getScannedProducts().size();
        tvItemCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, productCount, productCount)));
        findViewById(R.id.iv_expandable_indicator).setVisibility(View.GONE);
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
        View orderItemView = LayoutInflater.from(this).inflate(R.layout.do_details_list_item, llItemListLayout, false);
        if (product == null) return orderItemView;

        CustomTextView tvProductName = orderItemView.findViewById(R.id.product_name_text_view);
        CustomTextView tvProductQuantity = orderItemView.findViewById(R.id.quantity_text_view);
        CustomTextView tvAmount = orderItemView.findViewById(R.id.amount_text_view);
        CustomTextView tvUnits = orderItemView.findViewById(R.id.units_text_view);
        CheckBox productCheckbox = orderItemView.findViewById(R.id.product_checkbox);
        productCheckbox.setVisibility(View.GONE);
        tvProductName.setText(StringUtils.toString(product.getProductName(), ""));
        tvProductQuantity.setText(getString(R.string.weight_with_units, product.getWeight(), product.getWeightUnit()));
        tvUnits.setText(String.valueOf(product.getQuantity()));
        double value = product.getQuantity() * product.getPrice();
        tvAmount.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.format(Locale.getDefault(), "%.02f", value)));
        return orderItemView;
    }

    private void setClickListeners() {
        btTakeReturns.setOnClickListener(this);
        btSkipPayment.setOnClickListener(this);
        btCollectPayment.setOnClickListener(this);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;

            case R.id.bt_skip_payment:
                onSkipPaymentTap();
                break;

            case R.id.bt_collect_payment:
                onCollectPaymentTap();
                break;
        }
    }

    private void onSkipPaymentTap(){
        Intent intent = new Intent(this, NewSaleConfirmationActivity.class);
        intent.putExtra(IntentConstants.PAYMENT_COLLECTED, 0);
        intent.putExtra(IntentConstants.PAYMENT_SKIPPED, true);
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, viewModel.getConsumerLocation());
        intent.putParcelableArrayListExtra(IntentConstants.PRODUCT_LIST, (ArrayList<Product>) viewModel.getScannedProducts());
        startActivity(intent);
    }

    private void onCollectPaymentTap(){
        Intent intent = new Intent(this, CashSalesInvoiceActivity.class);
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, viewModel.getConsumerLocation());
        intent.putParcelableArrayListExtra(IntentConstants.PRODUCT_LIST, (ArrayList<Product>) viewModel.getScannedProducts());
        startActivity(intent);
    }
}
