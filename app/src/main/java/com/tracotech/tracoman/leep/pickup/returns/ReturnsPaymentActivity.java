package com.tracotech.tracoman.leep.pickup.returns;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.helpers.uimodels.Location;
import com.tracotech.tracoman.helpers.uimodels.Product;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.utils.DateTimeUtils;
import com.tracotech.tracoman.utils.StringUtils;
import com.tracotech.tracoman.viewmodels.pickup.returns.ReturnsInvoiceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsPaymentActivity extends ParentAppCompatActivity {
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
    @BindView(R.id.ll_item_list_layout)
    LinearLayout llItemListLayout;
    @BindView(R.id.confirm_button)
    Button btConfirm;
    @BindView(R.id.ll_item_summary_layout)
    LinearLayout llItemSummaryLayout;

    @BindView(R.id.tv_returned)
    TextView tvReturned;

    @BindView(R.id.tv_outstanding_balance)
    TextView tvPreviousBalance;
    @BindView(R.id.tv_grand_total)
    TextView tvGrandTotal;
    @BindView(R.id.et_payment_collected)
    EditText etPaymentCollected;

    private ReturnsInvoiceViewModel viewModel;

    private UILevelNetworkCallback locationNetworkCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            dismissProgressDialog();
            if (uiModels == null) {
                if (toLogout) {
                    logoutUser();
                } else if (isDialogToBeShown){
                    showNetworkRelatedDialogs(errorMessage);
                }
            } else if (uiModels.size() > 0) {
                runOnUiThread(() -> {
                    Location location = (Location) uiModels.get(0);
                    onLocationDetailsFetched(location);
                });
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_returns_payment);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(ReturnsInvoiceViewModel.class);
        ButterKnife.bind(this);
        extractIntentData();
        initialiseToolbar();
        setClickListeners();
        updateTopLayoutUI();
        updateItemSummaryUI();
        fetchLocationDetails();
        displayProductList();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent == null) return;
        viewModel.setConsumerLocation(intent.getParcelableExtra(IntentConstants.CONSUMER_LOCATION));
        viewModel.setScannedProducts(intent.getParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST));
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
    }

    private void setClickListeners() {
        btConfirm.setOnClickListener(this);
        llItemSummaryLayout.setOnClickListener(this);
    }

    private void fetchLocationDetails(){
        Customer consumer = viewModel.getConsumerLocation();
        if (consumer == null) return;
        showProgressDialog();
        viewModel.fetchBusinessLocation(consumer.getBusinessId(), consumer.getId(), locationNetworkCallback);
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
        int productCount = viewModel.getScannedProducts().size();
        tvItemCount.setText(Html.fromHtml(getResources().getQuantityString(R.plurals.item_count_text, productCount, productCount)));
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

    private void updateAmountDetails(double outstandingBalance) {
        double totalReturns = viewModel.totalReturnsValue();
        tvReturned.setText(StringUtils.amountToDisplay((float) totalReturns, this));
        tvPreviousBalance.setText(amountWithCurrencySymbol(outstandingBalance));
        tvGrandTotal.setText(amountWithCurrencySymbol(viewModel.grandTotal(totalReturns, outstandingBalance)));
        ((TextView) findViewById(R.id.tv_collected_amount_currency)).setText(AppUtils.userCurrencySymbol(this));
    }

    private String amountWithCurrencySymbol(double amount){
        return StringUtils.amountToDisplay((float) amount, this);
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

        TextView tvProductName = orderItemView.findViewById(R.id.product_name_text_view);
        TextView tvProductQuantity = orderItemView.findViewById(R.id.quantity_text_view);
        TextView tvAmount = orderItemView.findViewById(R.id.amount_text_view);
        TextView tvUnits = orderItemView.findViewById(R.id.units_text_view);
        CheckBox productCheckbox = orderItemView.findViewById(R.id.product_checkbox);
        TextView returnReasonTextView = orderItemView.findViewById(R.id.return_reason_tv);
        productCheckbox.setVisibility(View.GONE);
        tvProductName.setText(StringUtils.toString(product.getProductName(), ""));
        tvProductQuantity.setText(getString(R.string.weight_with_units, product.getWeight(), product.getWeightUnit()));
        tvUnits.setText(String.valueOf(product.getReturnQuantity()));
        tvAmount.setText(StringUtils.amountToDisplay((float) product.getTotalReturnsPrice(), this));
        returnReasonTextView.setText(product.getReturnReason().getReason());
        returnReasonTextView.setVisibility(View.VISIBLE);
        return orderItemView;
    }



    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;

            case R.id.confirm_button:
                gotoNextActivity();
                break;
            case R.id.ll_item_summary_layout:
                llItemListLayout.setVisibility(llItemListLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ((ImageView) findViewById(R.id.iv_expandable_indicator)).setImageResource(
                        llItemListLayout.getVisibility() == View.VISIBLE ? R.drawable.ic_expandable_indicator_close : R.drawable.ic_expandable_indicator_open);
                break;
        }
    }

    private void gotoNextActivity(){
        Double paymentCollected;
        if(!etPaymentCollected.getText().toString().isEmpty()) {
            paymentCollected = Double.valueOf(etPaymentCollected.getText().toString());
            Intent intent = new Intent(this, ReturnsPaymentMethodActivity.class);
            intent.putExtra(IntentConstants.PAYMENT_COLLECTED, paymentCollected);
            intent.putExtra(IntentConstants.PREVIOUS_BALANCE, viewModel.getOutstandingBalance());
            intent.putExtra(IntentConstants.CONSUMER_LOCATION, viewModel.getConsumerLocation());
            intent.putParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST,
                    (ArrayList<Product>) viewModel.getScannedProducts());
            startActivity(intent);
        }
    }

}
