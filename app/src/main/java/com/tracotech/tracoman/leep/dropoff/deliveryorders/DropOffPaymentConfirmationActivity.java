package com.tracotech.tracoman.leep.dropoff.deliveryorders;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.constants.PaymentMethod;
import com.tracotech.tracoman.helpers.customviews.ItemListDialogFragment;
import com.tracotech.tracoman.helpers.customviews.LeepSuccessDialog;
import com.tracotech.tracoman.helpers.customviews.SignatureDialogFragment;
import com.tracotech.tracoman.helpers.uihelpers.EditTextHelper;
import com.tracotech.tracoman.helpers.uihelpers.PrintableLine;
import com.tracotech.tracoman.helpers.uihelpers.PrinterHelper;
import com.tracotech.tracoman.interfaces.AddSignatureListener;
import com.tracotech.tracoman.interfaces.SuccessDialogEventListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.utils.DateTimeUtils;
import com.tracotech.tracoman.utils.LogUtils;
import com.tracotech.tracoman.utils.StringUtils;
import com.tracotech.tracoman.viewmodels.dropoff.deliveryorders.DropOffPaymentConfirmationViewModel;

import java.io.File;
import java.util.List;

public class DropOffPaymentConfirmationActivity extends ParentAppCompatActivity implements AddSignatureListener {

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

    private PrinterHelper printerHelper;

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
        editTextHelper.attachTextChangedListener(etReceivedFrom);
        editTextHelper.attachTextChangedListener(etContactNumber);
    }

    private EditTextHelper editTextHelper = new EditTextHelper(editable -> {
        if (editable == etReceivedFrom.getEditableText()) {
            tvReceivedFromError.setVisibility(etReceivedFrom.getText().length() > 0 ? View.GONE : View.VISIBLE);
        } else if (editable == etContactNumber.getEditableText()) {
            int contactNumberLength = etContactNumber.getText().length();
            tvContactNumberError.setVisibility(contactNumberLength == 0 || (contactNumberLength >= AppConstants.PHONE_MIN_LENGTH) ? View.GONE : View.VISIBLE);
        }
    });


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
        fetchDeliveryOrderItems();
    }

    private void fetchDeliveryOrderItems() {
        if (viewModel.getSelectedOrderItems() == null || viewModel.getSelectedOrderItems().size() == 0)
            viewModel.setOrderItems(viewModel.getOrderItems());
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
        tvCurrentSales.setText(StringUtils.amountToDisplay((float) viewModel.getCurrentSale(), this));
        tvPreviousBalance.setText(StringUtils.amountToDisplay((float) viewModel.getPreviousBalance(), this));
        tvOutstandingBalance.setText(StringUtils.amountToDisplay((float) viewModel.getOutstandingBalance(), this));
        tvGrandTotal.setText(StringUtils.amountToDisplay((float) viewModel.getGrandTotal(), this));
        tvPaymentCollected.setText(StringUtils.amountToDisplay((float) viewModel.getPaymentCollected(), this));
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
            runOnUiThread(this::dismissProgressDialog);
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
                printInvoice();
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
        int contactNumberLength = etContactNumber.getText().length();
        boolean isContactNumberValid = contactNumberLength == 0 || (contactNumberLength >= AppConstants.PHONE_MIN_LENGTH);
        boolean isReceiverValid = etReceivedFrom.getText().length() > 0;

        tvReceivedFromError.setVisibility(isReceiverValid ? View.GONE : View.VISIBLE);
        tvContactNumberError.setVisibility(isContactNumberValid ? View.GONE : View.VISIBLE);
        tvSignatureError.setVisibility(viewModel.isSignatureAdded() ? View.GONE : View.VISIBLE);
        return isContactNumberValid && viewModel.isSignatureAdded() && isReceiverValid;
    }

    private void sendSuccessBroadcast() {
        Intent intent = new Intent(IntentConstants.TASK_SUCCESSFUL);
        intent.putExtra(IntentConstants.TASK_SUCCESSFUL, true);
        intent.putExtra(IntentConstants.SUCCESS_TAG, AppConstants.TAG_DROPOFF);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void printInvoice() {
        printerHelper = new PrinterHelper(this);
        List<PrintableLine> printableLines = printerHelper.generateDeliveryOrderPrintableLines(
                viewModel.getDeliveryOrder(), viewModel.getDoItems(),
                AppUtils.userCurrencySymbol(DropOffPaymentConfirmationActivity.this),
                viewModel.getPaymentCollected(), false);
        printerHelper.print(printableLines, this);
    }

    @Override
    public void onDestroy() {
        if (printerHelper != null)
            printerHelper.closeService();
        super.onDestroy();
    }
}
