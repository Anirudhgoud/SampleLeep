package com.goleep.driverapp.leep.pickup.returns;


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

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.OrderItemsListAdapter;
import com.goleep.driverapp.adapters.ProductSearchArrayAdapter;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customviews.CustomAppCompatAutoCompleteTextView;
import com.goleep.driverapp.helpers.customviews.CustomEditText;
import com.goleep.driverapp.helpers.uihelpers.BarcodeScanHelper;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.BarcodeScanListener;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.pickup.returns.ReturnsSelectProductViewModel;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnsSelectProductActivity extends ParentAppCompatActivity implements AdapterView.OnItemClickListener {

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

    private ReturnsSelectProductViewModel viewModel;
    private OrderItemsListAdapter cashSalesListAdapter;
    private ProductSearchArrayAdapter productSearchArrayAdapter;
    private BarcodeCapture barcodeCapture;

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
            runOnUiThread(() -> Toast.makeText(ReturnsSelectProductActivity.this, reason, Toast.LENGTH_SHORT).show());
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

    private UILevelNetworkCallback returnReasonsCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> { };

    private void pauseBarcodeScanning() {
        barcodeCapture.pause();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_cash_sales_select_products);
    }

    @Override
    public void doInitialSetup() {
        viewModel = ViewModelProviders.of(this).get(ReturnsSelectProductViewModel.class);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        StockProductEntity stockProductEntity = (StockProductEntity) parent.getAdapter().getItem(position);
        if (stockProductEntity == null) return;
        Product product = viewModel.getProductFromStockProduct(stockProductEntity, AppConstants.TYPE_RETURNED);
        addProductToSelectedList(product);
    }


    private void extractIntentData() {
        Intent intent = getIntent();
        viewModel.setConsumerLocation(intent.getParcelableExtra(IntentConstants.CONSUMER_LOCATION));

    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.returns), R.drawable.ic_returns_title_icon);
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
        etUnits.setKeyImeChangeListener(this::hideUpdateQuantityView);
        etUnits.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                hideUpdateQuantityView();
                AppUtils.hideKeyboard(etUnits);
            }
            return true;
        });
        etUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
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
            }
        });
    }

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
        atvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) if (s.length() > 2) onProductSearch(s.toString(), locationId);
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        cashSalesListAdapter = new OrderItemsListAdapter(viewModel.getScannedProducts());
        cashSalesListAdapter.setOrderItemClickEventListener(deliveryOrderItemEventListener);
        cashSalesRecyclerView.setAdapter(cashSalesListAdapter);
    }

    private void onBarcodeDetected(String barcode) {
        Customer customer = viewModel.getConsumerLocation();
        if (customer == null) return;
        viewModel.returnableProducts(customer.getId(), null, barcode, productHavingBarcodeCallback);
    }

    private void addProductToSelectedList(Product product) {
        if (viewModel.isProductInScannedList(product.getId())) {
            resumeBarcodeScanning();
            Toast.makeText(this, R.string.item_already_added, Toast.LENGTH_SHORT).show();
        } else {
            displayUpdateQuantityView(product);
        }
    }

    private void onUpdateButtonTap() {
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
            intent.putParcelableArrayListExtra(IntentConstants.RETURN_REASONS, returnReasons);
            intent.putExtra(IntentConstants.FLOW, AppConstants.RETURNS_FLOW);
            startActivityForResult(intent, RETURN_REASON_REQUEST_CODE);
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

    private void updateProductDetails(ReturnReason returnReason) {
        Product product = viewModel.getSelectedProduct();
        if (product == null) return;

        product.setReturnReason(returnReason);
        if (!viewModel.isProductInScannedList(product.getId())) {
            viewModel.addToScannedProduct(product);
            btConfirm.setVisibility(View.VISIBLE);
        }
        cashSalesListAdapter.notifyDataSetChanged();
        hideUpdateQuantityView();
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) currentFocus.clearFocus();
        AppUtils.hideKeyboard(etUnits);
    }

    private void resumeBarcodeScanning() {
        barcodeCapture.resume();
    }

    private void onConfirmButtonTap() {
        ArrayList<Product> productList = viewModel.getScannedProducts();
        if (productList.size() > 0) {
            gotoNextActivity(viewModel.getConsumerLocation(), productList);
        }
    }

    private void displayProductNotAvailableToast(){
        Toast.makeText(this, R.string.invalid_return_product, Toast.LENGTH_SHORT).show();
    }

    private void gotoNextActivity(Customer consumerLocation, ArrayList<Product> productList) {
        Intent intent = new Intent(this, ReturnItemsConfirmActivity.class);
        intent.putExtra(IntentConstants.CONSUMER_LOCATION, consumerLocation);
        intent.putParcelableArrayListExtra(IntentConstants.SELECTED_PRODUCT_LIST, productList);
        intent.putExtra(IntentConstants.FLOW, AppConstants.RETURNS_FLOW);
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
