package com.goleep.driverapp.leep.pickup.returns;

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
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customviews.CashSalesReturnsListDialogFragment;
import com.goleep.driverapp.helpers.customviews.LeepSuccessDialog;
import com.goleep.driverapp.helpers.customviews.SignatureDialogFragment;
import com.goleep.driverapp.helpers.uihelpers.PrinterHelper;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.interfaces.AddSignatureListener;
import com.goleep.driverapp.interfaces.SuccessDialogEventListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.dropoff.cashsales.CashSalesFinalConfirmationActivity;
import com.goleep.driverapp.leep.main.HomeActivity;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.printer.PrinterService;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.LogUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.pickup.returns.ReturnsFinalConfirmationViewModel;
import com.ngx.BluetoothPrinter;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsFinalConfirmationActivity extends ParentAppCompatActivity implements AddSignatureListener, TextWatcher {

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
    @BindView(R.id.tv_grand_total)
    TextView tvGrandTotal;
    @BindView(R.id.tv_payment_collected)
    TextView tvPaymentCollected;
    @BindView(R.id.tv_payment_method)
    TextView tvPaymentMethod;
    @BindView(R.id.tv_previous_balance)
    TextView tvPreviousBalance;
    @BindView(R.id.tv_outstanding_balance)
    TextView tvOutstandingBalance;

    @BindView(R.id.et_received_from)
    EditText etReceivedFrom;
    @BindView(R.id.et_contact_number)
    EditText etContactNumber;
    @BindView(R.id.iv_signature)
    ImageView ivSignature;
    @BindView(R.id.tv_received_from_error)
    TextView tvReceivedFromError;
    @BindView(R.id.tv_contact_number_error)
    TextView tvContactNumberError;
    @BindView(R.id.tv_signature_error)
    TextView tvSignatureError;

    private ReturnsFinalConfirmationViewModel viewModel;

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
                    btContinue.setVisibility(View.VISIBLE);
                    Location location = (Location) uiModels.get(0);
                    onLocationDetailsFetched(location);
                });
            }
        }
    };

    private UILevelNetworkCallback returnsNetworkCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            runOnUiThread(() -> {
                dismissProgressDialog();
                if (uiModels == null) {
                    if (toLogout) {
                        logoutUser();
                    } else if (isDialogToBeShown) {
                        showNetworkRelatedDialogs(errorMessage);
                    }
                } else {
                    sendSuccessBroadcast();
                    showSuccessDialog();
                    LogUtils.debug(this.getClass().getSimpleName(), "Returns order successful");
                }
            });
        }
    };



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_returns_final_confirmation);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(ReturnsFinalConfirmationViewModel.class);
        ButterKnife.bind(this);
        extractIntentData();
        initialiseToolbar();
        setListeners();
        updateTopLayoutUI();
        updateAmountLayoutUI();
        fetchLocationDetails();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent == null) return;
        viewModel.setConsumerLocation(intent.getParcelableExtra(IntentConstants.CONSUMER_LOCATION));
        viewModel.setPreviousBalance(intent.getDoubleExtra(IntentConstants.PREVIOUS_BALANCE, 0.0));
        viewModel.setPaymentCollected(intent.getDoubleExtra(IntentConstants.PAYMENT_COLLECTED, 0.0));
        viewModel.setPaymentMethod(intent.getStringExtra(IntentConstants.PAYMENT_METHOD));
        List<Product> selectedProducts = intent.getParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST);
        viewModel.setScannedProducts(selectedProducts);
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
    }

    private void setListeners() {
        btContinue.setOnClickListener(this);
        ivSignature.setOnClickListener(this);
        btViewItemList.setOnClickListener(this);
        etReceivedFrom.addTextChangedListener(this);
        etContactNumber.addTextChangedListener(this);
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

    private void updateAmountLayoutUI() {
        double returnedAmount = viewModel.getTotalReturns();
        double previousBalance = viewModel.getPreviousBalance();
        double paymentCollected = viewModel.getPaymentCollected();
        double grandTotal = viewModel.grandTotal(returnedAmount, previousBalance);
        double outstandingBalance = viewModel.outstandingBalance(grandTotal, paymentCollected);
        String paymentMethod = viewModel.getPaymentMethod();

        tvReturned.setText(amountWithCurrencySymbol(returnedAmount));
        tvPreviousBalance.setText(amountWithCurrencySymbol(previousBalance));
        tvPaymentCollected.setText(amountWithCurrencySymbol(paymentCollected));
        tvGrandTotal.setText(amountWithCurrencySymbol(grandTotal));
        tvOutstandingBalance.setText(amountWithCurrencySymbol(outstandingBalance));
        tvPaymentMethod.setText(String.format(getString(R.string.payment_method_label), paymentMethod));
        if(paymentMethod != null && !paymentMethod.isEmpty())
            tvPaymentMethod.setVisibility(View.VISIBLE);
    }

    private String amountWithCurrencySymbol(Object amount) {
        return getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(this), String.valueOf(amount));
    }

    private void fetchLocationDetails(){
        Customer consumer = viewModel.getConsumerLocation();
        if (consumer == null) return;
        showProgressDialog();
        viewModel.fetchBusinessLocation(consumer.getBusinessId(), consumer.getId(), locationNetworkCallback);
    }

    private void onLocationDetailsFetched(Location location){
        if (location == null) return;
        String address = StringUtils.getAddress(location, viewModel.getConsumerLocation());
        tvAddress.setText(address);
        viewModel.updateAreaInConsumerLocation(address);
        viewModel.setPreviousBalance(location.getOutstandingBalance());
        updateAmountLayoutUI();
    }

    private void showSuccessDialog() {
        LeepSuccessDialog successDialog = new LeepSuccessDialog(this, getString(R.string.returns_success));
        successDialog.show();
        successDialog.setSuccessDialogEventListener(new SuccessDialogEventListener() {
            @Override
            public void onOkButtonTap() {
                goBackToHomeScreen();
            }

            @Override
            public void onCloseButtonTap() {
                goBackToHomeScreen();
            }

            @Override
            public void onPrintButtonTap() {
                printInvoice();
            }
        });
        successDialog.setPrintButtonVisibility(true);
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

            case R.id.iv_signature:
                showSignatureDialog();
                break;

            case R.id.bt_view_item_list:
                showItemListDialog();
                break;
        }
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

    private void onContinueButtonTap() {
        if (checkValidations()) {
            createReturnsOrder();
        }
    }

    private boolean checkValidations() {
        tvReceivedFromError.setVisibility(etReceivedFrom.getText().length() > 0 ? View.GONE : View.VISIBLE);
        int contactNumberLength = etContactNumber.getText().length();
        tvContactNumberError.setVisibility(contactNumberLength > 0 ? (contactNumberLength == 10 ? View.GONE : View.VISIBLE) : View.GONE);
        tvSignatureError.setVisibility(viewModel.isSignatureAdded() ? View.GONE : View.VISIBLE);
        return etReceivedFrom.getText().length() > 0 && viewModel.isSignatureAdded() && (contactNumberLength == 0 || contactNumberLength == 10);
    }

    private void createReturnsOrder() {
        showProgressDialog();
        String contactNo = etContactNumber.getText().length() == 0 ? null : etContactNumber.getText().toString();
        File file = AppUtils.fileFromBitmap(getApplicationContext(), AppUtils.bitmapFromView(ivSignature, ivSignature.getWidth(), ivSignature.getHeight()), viewModel.RECEIVER_SIGNATURE);
        viewModel.createReturnsDeliveryOrder(etReceivedFrom.getText().toString(), contactNo, file, returnsNetworkCallback);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable == etReceivedFrom.getEditableText()) {
            tvReceivedFromError.setVisibility(etReceivedFrom.getText().length() > 0 ? View.GONE : View.VISIBLE);
        } else if (editable == etContactNumber.getEditableText()) {
            tvContactNumberError.setVisibility(etContactNumber.getText().length() == 10 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onSignatureAdded(Bitmap signatureBitmap) {
        ivSignature.setImageBitmap(signatureBitmap);
        viewModel.setSignatureAdded(true);
        tvSignatureError.setVisibility(View.GONE);
    }

    private void goBackToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentConstants.TASK_SUCCESSFUL, true);
        startActivity(intent);
    }

    private void sendSuccessBroadcast(){
        Intent intent = new Intent(IntentConstants.TASK_SUCCESSFUL);
        intent.putExtra(IntentConstants.TASK_SUCCESSFUL, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void printInvoice(){
        BluetoothPrinter bluetoothPrinter = PrinterService.sharedInstance().getPrinter();
        bluetoothPrinter.initService(ReturnsFinalConfirmationActivity.this);
        if(bluetoothPrinter.getState() == BluetoothPrinter.STATE_CONNECTED) {
            new PrinterHelper().printInvoice(ReturnsFinalConfirmationActivity.this, null,
                    String.valueOf(viewModel.getRoNumber()), viewModel.getConsumerLocation(),
                    viewModel.getScannedProducts(), bluetoothPrinter,
                    AppUtils.userCurrencySymbol(ReturnsFinalConfirmationActivity.this),
                    viewModel.getPaymentCollected());
        } else {
            bluetoothPrinter.showDeviceList(ReturnsFinalConfirmationActivity.this);
        }
    }
}
