package com.tracotech.tracoman.leep.dropoff.cashsales;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.constants.PaymentMethod;
import com.tracotech.tracoman.helpers.customviews.CashSalesReturnsListDialogFragment;
import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.helpers.uimodels.Product;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.utils.DateTimeUtils;
import com.tracotech.tracoman.utils.StringUtils;
import com.tracotech.tracoman.viewmodels.dropoff.cashsales.CashSalesPaymentMethodViewModel;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesPaymentMethodActivity extends ParentAppCompatActivity {

    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;
    @BindView(R.id.tv_store_address)
    TextView tvAddress;
    @BindView(R.id.tv_date)
    TextView tvCurrentDate;
    @BindView(R.id.tv_time)
    TextView tvCurrentTime;
    @BindView(R.id.bt_continue)
    Button btContinue;
    @BindView(R.id.bt_view_item_list)
    Button btViewItemList;

    @BindView(R.id.tv_returned)
    TextView tvReturned;
    @BindView(R.id.tv_current_sale)
    TextView tvCurrentSales;
    @BindView(R.id.tv_previous_balance)
    TextView tvPreviousBalance;
    @BindView(R.id.tv_grand_total)
    TextView tvGrandTotal;
    @BindView(R.id.tv_payment_collected)
    TextView tvPaymentCollected;
    @BindView(R.id.tv_outstanding_balance)
    TextView tvOutstandingBalance;

    private CashSalesPaymentMethodViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_cash_sales_payment_method);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(CashSalesPaymentMethodViewModel.class);
        ButterKnife.bind(this);
        extractIntentData();
        initialiseToolbar();
        setClickListeners();
        updateTopLayoutUI();
        updateAmounts();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent == null) return;
        viewModel.setConsumerLocation(intent.getParcelableExtra(IntentConstants.CONSUMER_LOCATION));
        viewModel.setScannedProducts(intent.getParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST));
        viewModel.setPreviousBalance(intent.getDoubleExtra(IntentConstants.PREVIOUS_BALANCE, 0.0));
        viewModel.setPaymentCollected(intent.getDoubleExtra(IntentConstants.PAYMENT_COLLECTED, 0.0));
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

    private void updateAmounts() {
        double returnedAmount = viewModel.getTotalReturns();
        double currentSalesAmount = viewModel.getCurrentSales();
        double previousBalance = viewModel.getPreviousBalance();
        double paymentCollected = viewModel.getPaymentCollected();
        double grandTotal = viewModel.grandTotal(returnedAmount, currentSalesAmount, previousBalance);
        double outstandingBalance = viewModel.outstandingBalance(grandTotal, paymentCollected);

        tvReturned.setText(amountWithCurrencySymbol(returnedAmount));
        tvCurrentSales.setText(amountWithCurrencySymbol(currentSalesAmount));
        tvPreviousBalance.setText(amountWithCurrencySymbol(previousBalance));
        tvPaymentCollected.setText(amountWithCurrencySymbol(paymentCollected));
        tvGrandTotal.setText(amountWithCurrencySymbol(grandTotal));
        tvOutstandingBalance.setText(amountWithCurrencySymbol(outstandingBalance));
    }

    private String amountWithCurrencySymbol(double amount) {
        return StringUtils.amountToDisplay((float) amount, this);
    }

    private void setClickListeners() {
        btContinue.setOnClickListener(this);
        btViewItemList.setOnClickListener(this);
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

            case R.id.bt_view_item_list:
                showItemListDialog();
                break;
        }
    }

    private void showItemListDialog() {
        String fragmentTag = CashSalesReturnsListDialogFragment.class.getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
        DialogFragment itemListDialogFragment = CashSalesReturnsListDialogFragment.newInstance(viewModel.getScannedProducts());
        itemListDialogFragment.show(fragmentTransaction, fragmentTag);
    }

    private void onContinueButtonTap() {
        Intent intent = new Intent(this, CashSalesFinalConfirmationActivity.class);
        intent.putExtra(IntentConstants.PAYMENT_COLLECTED, viewModel.getPaymentCollected());
        intent.putExtra(IntentConstants.PREVIOUS_BALANCE, viewModel.getPreviousBalance());
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, viewModel.getConsumerLocation());
        intent.putExtra(IntentConstants.PAYMENT_METHOD, PaymentMethod.CASH);
        intent.putParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST, (ArrayList<Product>) viewModel.getScannedProducts());
        startActivity(intent);
    }

}
