package com.tracotech.tracoman.leep.dropoff.cashsales;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.OrderItemsListAdapter;
import com.tracotech.tracoman.adapters.ProductSearchArrayAdapter;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.helpers.customviews.CustomEditText;
import com.tracotech.tracoman.helpers.customviews.CustomAppCompatAutoCompleteTextView;
import com.tracotech.tracoman.helpers.uihelpers.BarcodeScanHelper;
import com.tracotech.tracoman.helpers.uihelpers.EditTextHelper;
import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.helpers.uimodels.Product;
import com.tracotech.tracoman.helpers.uimodels.ReturnReason;
import com.tracotech.tracoman.interfaces.BarcodeScanListener;
import com.tracotech.tracoman.interfaces.DeliveryOrderItemEventListener;
import com.tracotech.tracoman.interfaces.EditTextListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.leep.pickup.returns.ReturnsSelectReasonActivity;
import com.tracotech.tracoman.services.room.entities.StockProductEntity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.viewmodels.dropoff.cashsales.SelectReturnsProductViewModel;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectReturnsProductActivity extends ParentAppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.update_quantity_view)
    LinearLayout updateQuantityLayout;
    @BindView(R.id.et_units)
    CustomEditText etUnits;
    @BindView(R.id.product_name_text_view)
    TextView tvProductName;
    @BindView(R.id.invalid_quantity_error)
    TextView invalidQuantityError;
    @BindView(R.id.bt_update)
    Button btUpdate;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    @BindView(R.id.atv_search)
    CustomAppCompatAutoCompleteTextView atvSearch;
    @BindView(R.id.cash_sales_recyclerview)
    RecyclerView cashSalesRecyclerView;

    private final int RETURN_REASON_REQUEST_CODE = 101;

    private SelectReturnsProductViewModel viewModel;
    private OrderItemsListAdapter returnsListAdapter;
    private ProductSearchArrayAdapter productSearchArrayAdapter;
    private BarcodeCapture barcodeCapture;

    private UILevelNetworkCallback returnReasonsCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> { };

    private BarcodeScanListener barcodeScanListener = new BarcodeScanListener() {
        @Override
        public void onBarcodeScan(Barcode barcode) {
            runOnUiThread(() -> {
                pauseBarcodeScanning();
                onBarcodeDetected(barcode.displayValue);
            });
        }

        @Override
        public void onBarcodeScanFailure(String reason) {
            runOnUiThread(() -> Toast.makeText(SelectReturnsProductActivity.this, reason, Toast.LENGTH_SHORT).show());
        }
    };

    private DeliveryOrderItemEventListener deliveryOrderItemEventListener = new DeliveryOrderItemEventListener() {
        @Override
        public void onUnitsTap(int itemId, int maxUnits) {
            displayUpdateQuantityView(viewModel.getProductFromScannedProducts(itemId));
        }

        @Override
        public void onCheckboxTap(int itemId, boolean isChecked) {
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_cash_sales_select_products);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(SelectReturnsProductViewModel.class);
        ButterKnife.bind(this);
        extractIntentData();
        initialiseToolbar();
        initialiseTabBar();
        initialiseBarcodeScanner();
        initialiseRecyclerView();
        initialiseAutoCompleteTextView();
        setClickListeners();
        fetchReturnReasons();
        initialiseUpdateQuantityView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeCapture.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeCapture.onResume();
    }

    private void extractIntentData() {
        Intent intent = getIntent();
        viewModel.setConsumerLocation(intent.getParcelableExtra(IntentConstants.CONSUMER_LOCATION));
        viewModel.setSelectedProducts(intent.getParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST));
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.cash_sales_returns_title), R.drawable.ic_cash_sales);
    }

    private void initialiseTabBar() {
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View barcodeCaptureView = barcodeCapture.getView();
                switch (tab.getPosition()) {
                    case 0:
                        barcodeCapture.onResume();
                        atvSearch.setVisibility(View.GONE);
                        if (barcodeCaptureView != null) barcodeCaptureView.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        AppUtils.hideKeyboard(getCurrentFocus());
                        atvSearch.setVisibility(View.VISIBLE);
                        if (barcodeCaptureView != null) barcodeCaptureView.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        barcodeCapture.onPause();
                        break;
                    case 1:
                        atvSearch.clearFocus();
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupTabIcons() {
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(getString(R.string.scan), getResources().getDrawable(R.drawable.ic_scan_barcode))));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(getString(R.string.search), getResources().getDrawable(R.drawable.ic_search))));
    }

    private View getTabView(String title, Drawable iconDrawable) {
        View listTab = LayoutInflater.from(this).inflate(R.layout.custom_tab_item_layout, null);
        TextView textView = listTab.findViewById(R.id.title_text);
        ImageView icon = listTab.findViewById(R.id.icon);
        listTab.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setText(title);
        icon.setImageDrawable(iconDrawable);
        return listTab;
    }

    private void initialiseBarcodeScanner() {
        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        BarcodeScanHelper barcodeScanHelper = new BarcodeScanHelper();
        barcodeScanHelper.setBarcodeScanListener(barcodeScanListener);
        barcodeCapture.setRetrieval(barcodeScanHelper);
        barcodeCapture.setSupportMultipleScan(false);
        barcodeCapture.setShowDrawRect(true);
    }

    private void initialiseUpdateQuantityView() {
        btUpdate.setEnabled(false);
        etUnits.setKeyImeChangeListener(this::hideUpdateQuantityView);
        etUnits.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                onUpdateButtonTap();
            }
            return true;
        });
        EditTextHelper editTextHelper = new EditTextHelper(unitsChangeListener);
        editTextHelper.attachTextChangedListener(etUnits);
    }

    private EditTextListener unitsChangeListener = editable -> {
        Product selectedProduct = viewModel.getSelectedProduct();
        if (selectedProduct == null) return;
        int maxUnits = selectedProduct.getMaxQuantity();
        String newUnitsText = etUnits.getText().toString();
        if (newUnitsText.length() > 0) {
            int newUnits = Integer.valueOf(newUnitsText);
            boolean isValid = maxUnits == -1 ? newUnits != 0 : (newUnits <= maxUnits && newUnits != 0);
            invalidQuantityError.setVisibility(isValid ? View.INVISIBLE : View.VISIBLE);
            btUpdate.setEnabled(isValid);
        } else {
            btUpdate.setEnabled(false);
        }
    };

    private void fetchReturnReasons(){
        viewModel.fetchReturnReasons(returnReasonsCallback);
    }

    private void initialiseAutoCompleteTextView() {
        Drawable rightDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_search_inactive);
        atvSearch.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
        Customer customer = viewModel.getConsumerLocation();
        if (customer == null) return;
        int locationId = customer.getId();
        productSearchArrayAdapter = new ProductSearchArrayAdapter(this, new ArrayList());
        atvSearch.setAdapter(productSearchArrayAdapter);
        atvSearch.setOnItemClickListener(this);
        EditTextHelper editTextHelper = new EditTextHelper(editable -> {
            if (editable.length() > 2) onProductSearch(editable.toString(), locationId);
        });
        editTextHelper.attachTextChangedListener(atvSearch);
        atvSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                hideUpdateQuantityView();
                AppUtils.hideKeyboard(etUnits);
            }
        });
    }

    private void onProductSearch(String text, int destinationLocationId){
        viewModel.returnableProducts(destinationLocationId, text, null, productSearchCallback);
    }

    private void setClickListeners() {
        btUpdate.setOnClickListener(this);
        btConfirm.setOnClickListener(this);
    }

    private void initialiseRecyclerView() {
        cashSalesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cashSalesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        returnsListAdapter = new OrderItemsListAdapter(viewModel.getScannedProducts());
        returnsListAdapter.setOrderItemClickEventListener(deliveryOrderItemEventListener);
        cashSalesRecyclerView.setAdapter(returnsListAdapter);
    }

    private void onBarcodeDetected(String barcode) {
        Customer customer = viewModel.getConsumerLocation();
        if (customer == null) return;
        viewModel.returnableProducts(customer.getId(), null, barcode, productHavingBarcodeCallback);
    }

    private void addProductToSelectedList(Product product) {
        if (viewModel.isProductInScannedList(product.getId())) {
            resumeBarcodeScanning();
            Toast.makeText(SelectReturnsProductActivity.this, R.string.item_already_added, Toast.LENGTH_SHORT).show();
        } else {
            displayUpdateQuantityView(product);
        }
    }

    private void displayUpdateQuantityView(Product product) {
        if (product != null) {
            viewModel.setSelectedProduct(product);
            etUnits.requestFocus();
            tvProductName.setText(product.getProductName() + " " + product.getWeight() + product.getWeightUnit());
            etUnits.setText("");
            etUnits.setHint(product.getMaxQuantity() == -1 ? "" :String.valueOf(product.getMaxQuantity()));
            btUpdate.setEnabled(true);
            updateQuantityLayout.setVisibility(View.VISIBLE);
            invalidQuantityError.setVisibility(View.INVISIBLE);
            AppUtils.showKeyboard(etUnits);
        }
    }

    private void hideUpdateQuantityView() {
        resumeBarcodeScanning();
        updateQuantityLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                AppUtils.hideKeyboard(getCurrentFocus());
                finish();
                break;

            case R.id.bt_update:
                onUpdateButtonTap();
                break;

            case R.id.bt_confirm:
                onConfirmButtonTap();
                break;
        }
    }

    private void onUpdateButtonTap() {
        if (!btUpdate.isEnabled()) return;
        updateProductDetails();
    }

    private UILevelNetworkCallback productHavingBarcodeCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> runOnUiThread(() -> {
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else if (isDialogToBeShown){
                showNetworkRelatedDialogs(errorMessage);
                resumeBarcodeScanning();
            }
        } else {
            onProductReceived(uiModels);
        }
    });

    private void onProductReceived(List<?> uiModels){
        if (uiModels.size() == 0) {
            displayProductNotAvailableToast();
            resumeBarcodeScanning();
        }
        else {
            StockProductEntity product = (StockProductEntity) uiModels.get(0);
            if (product == null){
                displayProductNotAvailableToast();
                resumeBarcodeScanning();
                return;
            }
            addProductToSelectedList(viewModel.getProductFromStockProduct(product, AppConstants.TYPE_RETURNED));
        }
    }

    private UILevelNetworkCallback productSearchCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> runOnUiThread(() -> {
        if (uiModels == null) {
            if (toLogout) logoutUser();
        } else {
            List<StockProductEntity> list = (List<StockProductEntity>) uiModels;
            productSearchArrayAdapter.updateData(list);
        }
    });

    private void updateProductDetails() {
        Product product = viewModel.getSelectedProduct();
        try {
            product.setQuantity(0);
            product.setReturnQuantity(Integer.valueOf(etUnits.getText().toString()));
            goToReturnReasons(product);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void goToReturnReasons(Product product) {
        ArrayList<ReturnReason> returnReasons = (ArrayList<ReturnReason>) viewModel.getReturnReasons();
        if(returnReasons != null && returnReasons.size() > 0) {
            Intent intent = new Intent(this, ReturnsSelectReasonActivity.class);
            intent.putExtra(IntentConstants.PRODUCT, product);
            intent.putExtra(IntentConstants.FLOW, AppConstants.CASH_SALES_FLOW);
            intent.putParcelableArrayListExtra(IntentConstants.RETURN_REASONS, returnReasons);
            startActivityForResult(intent, RETURN_REASON_REQUEST_CODE);
        }
    }

    private void updateProductDetails(ReturnReason returnReason) {
        Product product = viewModel.getSelectedProduct();
        if (product == null) return;
        product.setReturnReason(returnReason);
        if (!viewModel.isProductInScannedList(product.getId())) {
            viewModel.addToScannedProduct(product);
            btConfirm.setVisibility(View.VISIBLE);
        }
        returnsListAdapter.notifyDataSetChanged();
        hideUpdateQuantityView();
        getCurrentFocus().clearFocus();
        AppUtils.hideKeyboard(etUnits);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StockProductEntity stockProductEntity = (StockProductEntity) parent.getAdapter().getItem(position);
        if (stockProductEntity == null) return;
        Product product = viewModel.getProductFromStockProduct(stockProductEntity, AppConstants.TYPE_RETURNED);
        addProductToSelectedList(product);
    }

    private void pauseBarcodeScanning() {
        barcodeCapture.pause();
    }

    private void resumeBarcodeScanning() {
        barcodeCapture.resume();
    }

    private void onConfirmButtonTap() {
        if (viewModel.getScannedProducts().size() > 0) {
            gotoNextActivity();
        }
    }

    private void displayProductNotAvailableToast(){
        Toast.makeText(this, R.string.invalid_return_product, Toast.LENGTH_SHORT).show();
    }

    private void gotoNextActivity() {
        Intent intent = new Intent(this, CashSalesReturnsConfirmationActivity.class);
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, viewModel.getConsumerLocation());
        intent.putParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST, viewModel.getSelectedProducts());
        intent.putParcelableArrayListExtra(IntentConstants.RETURNED_PRODUCT_LIST, viewModel.getScannedProducts());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RETURN_REASON_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ReturnReason returnReason = data.getParcelableExtra(IntentConstants.RETURN_REASON);
                updateProductDetails(returnReason);
            }
        }
    }
}
