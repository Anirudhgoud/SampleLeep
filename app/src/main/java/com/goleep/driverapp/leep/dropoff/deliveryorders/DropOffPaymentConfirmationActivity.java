package com.goleep.driverapp.leep.dropoff.deliveryorders;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.constants.PaymentMethod;
import com.goleep.driverapp.helpers.customviews.ItemListDialogFragment;
import com.goleep.driverapp.helpers.customviews.LeepSuccessDialog;
import com.goleep.driverapp.helpers.customviews.SignatureDialogFragment;
import com.goleep.driverapp.interfaces.AddSignatureListener;
import com.goleep.driverapp.interfaces.SuccessDialogEventListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.printer.PrinterService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.LogUtils;
import com.goleep.driverapp.helpers.uihelpers.PrinterHelper;
import com.goleep.driverapp.viewmodels.dropoff.deliveryorders.DropOffPaymentConfirmationViewModel;
import com.ngx.BluetoothPrinter;

import java.io.File;
import java.util.List;

public class DropOffPaymentConfirmationActivity extends ParentAppCompatActivity implements AddSignatureListener, TextWatcher {

    private TextView tvCustomerName;
    private TextView tvStoreAddress;
    private TextView tvDoNumber;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvCurrentSales;
    private TextView tvOutstandingBalance;
    private TextView tvGrandTotal;
    private TextView tvPaymentCollected;
    private TextView tvPaymentMethod;
    private TextView tvPreviousBalance;
    private Button btConfirm;
    private Button btViewItemList;
    private EditText etReceivedFrom;
    private EditText etContactNumber;
    private ImageView ivSignature;
    private LinearLayout llCollectPayment;

    //Error views
    private TextView tvReceivedFromError, tvContactNumberError, tvSignatureError;

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
        ivSignature = findViewById(R.id.iv_signature);
        etReceivedFrom = findViewById(R.id.et_received_from);
        etContactNumber = findViewById(R.id.et_contact_number);
        tvReceivedFromError = findViewById(R.id.tv_received_from_error);
        tvContactNumberError = findViewById(R.id.tv_contact_number_error);
        tvSignatureError = findViewById(R.id.tv_signature_error);
    }

    private void addListeners() {
        btViewItemList.setOnClickListener(this);
        btConfirm.setOnClickListener(this);
        ivSignature.setOnClickListener(this);
        etReceivedFrom.addTextChangedListener(this);
        etContactNumber.addTextChangedListener(this);
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
            tvDate.setText(DateTimeUtils.currentDateToDisplay());
            tvTime.setText(DateTimeUtils.currentTimeToDisplay());
        }
    }

    private void updateSalesValues() {
        tvCurrentSales.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(viewModel.getCurrentSale())));
        tvPreviousBalance.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(viewModel.getPreviousBalance())));
        tvOutstandingBalance.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(viewModel.getOutstandingBalance())));
        tvGrandTotal.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(viewModel.getGrandTotal())));
        tvPaymentCollected.setText(getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(viewModel.getPaymentCollected())));
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

    private void showSignatureDialog() {
        String fragmentTag = SignatureDialogFragment.class.getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
        SignatureDialogFragment signatureDialogFragment = new SignatureDialogFragment();
        signatureDialogFragment.show(fragmentTransaction, fragmentTag);
    }

    @Override
    public void onSignatureAdded(Bitmap signatureBitmap) {
        ivSignature.setImageBitmap(signatureBitmap);
        viewModel.setSignatureAdded(true);
        tvSignatureError.setVisibility(View.GONE);
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

            case R.id.iv_signature:
                showSignatureDialog();
                break;

            case R.id.bt_confirm:
                if (checkValidations()) {
                    showProgressDialog();
                    viewModel.editDeliveryOrderWithSelectedProducts(editOrderNetworkCallback);
                }
                break;
        }
    }

    private UILevelNetworkCallback editOrderNetworkCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        if (uiModels == null) {
            runOnUiThread(() -> dismissProgressDialog());
            if (toLogout) {
                logoutUser();
            } else if (isDialogToBeShown) {
                showNetworkRelatedDialogs(errorMessage);
            }
        } else {
            deliverOrder();
        }
    };

    private void deliverOrder() {
        String contactNo = etContactNumber.getText().length() == 0 ? null : etContactNumber.getText().toString();
        File file = AppUtils.fileFromBitmap(getApplicationContext(), AppUtils.bitmapFromView(ivSignature, ivSignature.getWidth(), ivSignature.getHeight()), viewModel.RECEIVER_SIGNATURE);
        viewModel.deliverOrder(etReceivedFrom.getText().toString(), contactNo, file, deliverOrderNetworkCallback);
    }

    private UILevelNetworkCallback deliverOrderNetworkCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            runOnUiThread(() -> dismissProgressDialog());
            if (uiModels == null) {
                if (toLogout) {
                    logoutUser();
                } else if (isDialogToBeShown) {
                    showNetworkRelatedDialogs(errorMessage);
                }
            } else {
                runOnUiThread(() -> {
                    sendSuccessBroadcast();
                    showSuccessDialog();
                });
                LogUtils.debug(this.getClass().getSimpleName(), "Order Delivered");
            }
        }
    };

    private void showSuccessDialog() {
        LeepSuccessDialog successDialog = new LeepSuccessDialog(this, getString(R.string.delivery_successful));
        successDialog.show();
        successDialog.setSuccessDialogEventListener(new SuccessDialogEventListener() {
            @Override
            public void onOkButtonTap() {
                goBackToDeliveryList();
            }

            @Override
            public void onCloseButtonTap() {
                goBackToDeliveryList();
            }

            @Override
            public void onPrintButtonTap() {
                LogUtils.debug(this.getClass().getSimpleName(), "Print tapped");
                BluetoothPrinter printer = PrinterService.sharedInstance().getPrinter();
                if(printer.getState() == BluetoothPrinter.STATE_CONNECTED) {
                    new PrinterHelper().printInvoice(viewModel.getDeliveryOrder(), viewModel.getSelectedOrderItems(),
                            null, printer);
                } else {
                    printer.showDeviceList(DropOffPaymentConfirmationActivity.this);
                }
            }
        });
        successDialog.setPrintButtonVisibility(true);
    }

    private void goBackToDeliveryList() {
        Intent intent = new Intent(this, DropOffDeliveryOrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean checkValidations() {
        tvReceivedFromError.setVisibility(etReceivedFrom.getText().length() > 0 ? View.GONE : View.VISIBLE);
        int contactNumberLength = etContactNumber.getText().length();
        tvContactNumberError.setVisibility(contactNumberLength > 0 ? (contactNumberLength == 10 ? View.GONE : View.VISIBLE) : View.GONE);
        tvSignatureError.setVisibility(viewModel.isSignatureAdded() ? View.GONE : View.VISIBLE);
        return etReceivedFrom.getText().length() > 0 && viewModel.isSignatureAdded() && (contactNumberLength == 0 || contactNumberLength == 10);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == etReceivedFrom.getEditableText()) {
            tvReceivedFromError.setVisibility(etReceivedFrom.getText().length() > 0 ? View.GONE : View.VISIBLE);
        } else if (editable == etContactNumber.getEditableText()) {
            tvContactNumberError.setVisibility(etContactNumber.getText().length() == 10 ? View.GONE : View.VISIBLE);
        }
    }

    private void sendSuccessBroadcast(){
        Intent intent = new Intent(IntentConstants.TASK_SUCCESSFUL);
        intent.putExtra(IntentConstants.TASK_SUCCESSFUL, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
