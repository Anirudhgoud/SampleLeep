package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.customviews.ItemListDialogFragment;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.DropOffCollectPaymentMethodViewModel;

public class DropOffCollectPaymentMethodActivity extends ParentAppCompatActivity {

    private CustomTextView tvCustomerName;
    private CustomTextView tvStoreAddress;
    private CustomTextView tvDoNumber;
    private CustomTextView tvDate;
    private CustomTextView tvTime;
    private CustomTextView tvCurrentSales;
    private CustomTextView tvOutstandingBalance;
    private CustomTextView tvGrandTotal;
    private CustomTextView tvPaymentCollected;
    private CustomButton btContinue;
    private CustomButton btViewItemList;

    private DropOffCollectPaymentMethodViewModel viewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_dropoff_collect_payment_method);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(DropOffCollectPaymentMethodViewModel.class);
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
        btContinue = findViewById(R.id.bt_continue);
        btViewItemList = findViewById(R.id.bt_view_item_list);
    }

    private void addListeners() {
        btViewItemList.setOnClickListener(this);
        btContinue.setOnClickListener(this);
    }


    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.delivery_orders), R.drawable.ic_drop_off_toolbar);
    }

    private void getIntentExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            viewModel.setDeliveryOrderId(bundle.getInt(IntentConstants.DELIVERY_ORDER_ID));
            viewModel.setBusinessAddress(bundle.getString(IntentConstants.BUSINESS_ADDRESS));
            viewModel.setCurrentSale(bundle.getDouble(IntentConstants.CURRENT_SALE));
            viewModel.setOutstandingBalance(bundle.getDouble(IntentConstants.OUTSTANDING_BALANCE));
            viewModel.setPaymentCollected(bundle.getDouble(IntentConstants.PAYMENT_COLLECTED));
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
            tvDate.setText(viewModel.dateToDisplay(deliveryOrder.getPreferredDeliveryDate()));
            tvTime.setText(viewModel.currentTimeToDisplay());
        }
    }

    private void updateSalesValues() {
        tvCurrentSales.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getCurrentSale())));
        tvOutstandingBalance.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getOutstandingBalance())));
        tvGrandTotal.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getGrandTotal())));
        tvPaymentCollected.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), String.valueOf(viewModel.getPaymentCollected())));
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
