package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.constants.PaymentMethod;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.customviews.ItemListDialogFragment;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.DropOffPaymentConfirmationViewModel;

public class DropOffPaymentConfirmationActivity extends ParentAppCompatActivity {

    private CustomTextView tvCustomerName;
    private CustomTextView tvStoreAddress;
    private CustomTextView tvDoNumber;
    private CustomTextView tvDate;
    private CustomTextView tvTime;
    private CustomTextView tvCurrentSales;
    private CustomTextView tvOutstandingBalance;
    private CustomTextView tvGrandTotal;
    private CustomTextView tvPaymentCollected;
    private CustomTextView tvPaymentMethod;
    private CustomTextView tvPreviousBalance;
    private CustomButton btConfirm;
    private CustomButton btViewItemList;
    private LinearLayout llCollectPayment;

    private DropOffPaymentConfirmationViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_drop_off_payment_confirmation);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(DropOffPaymentConfirmationViewModel.class);
        getIntentExtras();
        initialiseToolbar();
        connectUIElements();
        addListeners();
        fetchDeliveryOrderData();
        updateDeliveryOrderUI();
        updateSalesValues();
    }

    private void connectUIElements() {
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvStoreAddress = findViewById(R.id.tv_store_address);
        tvDoNumber = findViewById(R.id.tv_do_number);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        tvCurrentSales = findViewById(R.id.tv_current_sales);
        tvOutstandingBalance = findViewById(R.id.tv_outstanding_balance);
        tvGrandTotal = findViewById(R.id.tv_grand_total);
        tvPaymentCollected = findViewById(R.id.tv_payment_collected);
        tvPaymentMethod = findViewById(R.id.tv_payment_method);
        tvPreviousBalance = findViewById(R.id.tv_previous_balance);
        btConfirm = findViewById(R.id.bt_confirm);
        btViewItemList = findViewById(R.id.bt_view_item_list);
        llCollectPayment = findViewById(R.id.ll_collect_payment_view);
    }

    private void addListeners() {
        btViewItemList.setOnClickListener(this);
        btConfirm.setOnClickListener(this);
    }


    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.do_payment_title), R.drawable.ic_drop_off_toolbar);
    }

    private void getIntentExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            viewModel.setDeliveryOrderId(bundle.getInt(IntentConstants.DELIVERY_ORDER_ID));
            viewModel.setBusinessAddress(bundle.getString(IntentConstants.BUSINESS_ADDRESS, ""));
            viewModel.setCurrentSale(bundle.getDouble(IntentConstants.CURRENT_SALE, 0));
            viewModel.setPreviousBalance(bundle.getDouble(IntentConstants.OUTSTANDING_BALANCE, 0));
            viewModel.setPaymentCollected(bundle.getDouble(IntentConstants.PAYMENT_COLLECTED, 0));
            viewModel.setPaymentMethod(bundle.getString(IntentConstants.PAYMENT_METHOD, PaymentMethod.CASH));
        }
    }

    private void fetchDeliveryOrderData() {
        viewModel.setDeliveryOrder(viewModel.deliveryOrder(viewModel.getDeliveryOrderId()));
    }

    private void fetchDeliveryOrderItems() {
        if (viewModel.getSelectedOrderItems() == null) {
            viewModel.setOrderItems(viewModel.getSelectedOrderItems());
        }
    }

    private void updateDeliveryOrderUI() {
        DeliveryOrderEntity deliveryOrder = viewModel.getDeliveryOrder();
        if (deliveryOrder != null) {
            tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
            tvStoreAddress.setText(viewModel.getBusinessAddress());
            tvDoNumber.setText(deliveryOrder.getDoNumber() == null ? "-" : deliveryOrder.getDoNumber());
            tvDate.setText(viewModel.currentDateToDisplay());
            tvTime.setText(viewModel.currentTimeToDisplay());
        }
    }

    private void updateSalesValues() {
        tvCurrentSales.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getCurrentSale())));
        tvPreviousBalance.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getPreviousBalance())));
        tvOutstandingBalance.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getOutstandingBalance())));
        tvGrandTotal.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getGrandTotal())));
        tvPaymentCollected.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getPaymentCollected())));
        tvPaymentMethod.setText(viewModel.getPaymentMethod());
        llCollectPayment.setVisibility(viewModel.getPaymentCollected() == 0 ? View.GONE : View.VISIBLE);
    }

    private void showItemListDialog() {
        String fragmentTag = ItemListDialogFragment.class.getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
        DialogFragment itemListDialogFragment = ItemListDialogFragment.newInstance(viewModel.getDeliveryOrderId(), viewModel.getGrandTotal());
        itemListDialogFragment.show(fragmentTransaction, fragmentTag);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;

            case R.id.bt_view_item_list:
                fetchDeliveryOrderItems();
                showItemListDialog();
                break;

            case R.id.bt_continue:
                break;
        }
    }
}
