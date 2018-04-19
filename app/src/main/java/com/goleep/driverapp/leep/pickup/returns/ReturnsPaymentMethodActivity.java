package com.goleep.driverapp.leep.pickup.returns;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.constants.PaymentMethod;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.customviews.CashSalesReturnsListDialogFragment;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.leep.dropoff.cashsales.CashSalesFinalConfirmationActivity;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.dropoff.cashsales.CashSalesPaymentMethodViewModel;
import com.goleep.driverapp.viewmodels.pickup.returns.ReturnsPaymentMethodViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsPaymentMethodActivity extends ParentAppCompatActivity {

    @BindView(R.id.tv_customer_name)
    CustomTextView tvCustomerName;
    @BindView(R.id.tv_store_address)
    CustomTextView tvAddress;
    @BindView(R.id.tv_date)
    CustomTextView tvCurrentDate;
    @BindView(R.id.tv_time)
    CustomTextView tvCurrentTime;
    @BindView(R.id.bt_continue)
    Button btContinue;
    @BindView(R.id.bt_view_item_list)
    Button btViewItemList;

    @BindView(R.id.tv_returned)
    TextView tvReturned;
    @BindView(R.id.tv_grand_total)
    TextView tvGrandTotal;
    @BindView(R.id.tv_payment_collected)
    TextView tvPaymentCollected;
    @BindView(R.id.tv_previous_balance)
    TextView tvPreviousBalance;
    @BindView(R.id.tv_outstanding_balance)
    TextView tvOutstandingBalance;

    private ReturnsPaymentMethodViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_returns_payment_method);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(ReturnsPaymentMethodViewModel.class);
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
        setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
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
        double previousBalance = viewModel.getPreviousBalance();
        double paymentCollected = viewModel.getPaymentCollected();
        double grandTotal = viewModel.grandTotal(returnedAmount, previousBalance);
        double outstandingBalance = viewModel.outstandingBalance(grandTotal, paymentCollected);

        tvReturned.setText(amountWithCurrencySymbol(returnedAmount));
        tvPreviousBalance.setText(amountWithCurrencySymbol(previousBalance));
        tvPaymentCollected.setText(amountWithCurrencySymbol(paymentCollected));
        tvGrandTotal.setText(amountWithCurrencySymbol(grandTotal));
        tvOutstandingBalance.setText(amountWithCurrencySymbol(outstandingBalance));
    }

    public String amountWithCurrencySymbol(Object amount) {
        return getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(amount));
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
        Intent intent = new Intent(this, ReturnsFinalConfirmationActivity.class);
        intent.putExtra(IntentConstants.PAYMENT_COLLECTED, viewModel.getPaymentCollected());
        intent.putExtra(IntentConstants.PREVIOUS_BALANCE, viewModel.getPreviousBalance());
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, viewModel.getConsumerLocation());
        intent.putExtra(IntentConstants.PAYMENT_METHOD, PaymentMethod.CASH);
        intent.putParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST, (ArrayList<Product>) viewModel.getScannedProducts());
        startActivity(intent);
    }

}