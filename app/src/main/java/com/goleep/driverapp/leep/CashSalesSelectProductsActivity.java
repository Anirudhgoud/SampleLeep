package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.CustomerSearchArrayAdapter;
import com.goleep.driverapp.adapters.OrderItemsListAdapter;
import com.goleep.driverapp.adapters.ProductSearchArrayAdapter;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.customviews.CustomAppCompatAutoCompleteTextView;
import com.goleep.driverapp.helpers.uihelpers.BarcodeScanHelper;
import com.goleep.driverapp.interfaces.BarcodeScanListener;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.CashSalesSelectProductsViewModel;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashSalesSelectProductsActivity extends ParentAppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.update_quantity_view)
    LinearLayout updateQuantityLayout;
    @BindView(R.id.et_units)
    CustomEditText etUnits;
    @BindView(R.id.product_name_text_view)
    CustomTextView tvProductName;
    @BindView(R.id.invalid_quantity_error)
    CustomTextView invalidQuantityError;
    @BindView(R.id.bt_update)
    CustomButton btUpdate;
    @BindView(R.id.atv_search)
    CustomAppCompatAutoCompleteTextView atvSearch;
    @BindView(R.id.cash_sales_recyclerview)
    RecyclerView cashSalesRecyclerView;

    private CashSalesSelectProductsViewModel viewModel;
    private OrderItemsListAdapter cashSalesListAdapter;
    private ProductSearchArrayAdapter productSearchArrayAdapter;
    private BarcodeCapture barcodeCapture;

    private BarcodeScanListener barcodeScanListener = new BarcodeScanListener() {
        @Override
        public void onBarcodeScan(Barcode barcode) {
            runOnUiThread(() -> {
                onBarcodeDetected(barcode.displayValue);
//                Toast.makeText(CashSalesSelectProductsActivity.this, barcode.displayValue, Toast.LENGTH_SHORT).show();
//                barcodeCapture.pause();
            });
        }

        @Override
        public void onBarcodeScanFailure(String reason) {
            runOnUiThread(() -> Toast.makeText(CashSalesSelectProductsActivity.this, reason, Toast.LENGTH_SHORT).show());
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
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        viewModel = ViewModelProviders.of(this).get(CashSalesSelectProductsViewModel.class);
        ButterKnife.bind(this);
        initialiseToolbar();
        initialiseTabBar();
        initialiseBarcodeScanner();
        initialiseRecyclerView();
        initialiseAutoCompleteTextView();
        initialiseUpdateQuantityView();
    }

    private void initialiseToolbar() {
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        setTitleIconAndText(getString(R.string.cash_sales), R.drawable.ic_cash_sales);
    }

    private void initialiseTabBar() {
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        barcodeCapture.onResume();
                        atvSearch.setVisibility(View.GONE);
                        barcodeCapture.getView().setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        //TODO: hide keyboard
                        //AppUtils.hideKeyboard();
                        atvSearch.setVisibility(View.VISIBLE);
                        barcodeCapture.getView().setVisibility(View.GONE);
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
        CustomTextView textView = listTab.findViewById(R.id.title_text);
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
    }

    private void initialiseUpdateQuantityView() {
        btUpdate.setOnClickListener(this);
        etUnits.setKeyImeChangeListener(this::hideUpdateQuantityView);
        etUnits.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                hideUpdateQuantityView();
                AppUtils.toggleKeyboard(etUnits, getApplicationContext());
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
                if (viewModel.getSelectedStockProductEntity() != null) {
                    int maxUnits = viewModel.getMaxQuantityofProduct(viewModel.getSelectedStockProductEntity().getId());
                    String newUnitsText = etUnits.getText().toString();
                    if (newUnitsText.length() > 0) {
                        int newUnits = Integer.valueOf(newUnitsText);
                        boolean isValid = newUnits <= maxUnits && newUnits != 0;
                        etUnits.setError(isValid ? null : invalidQuantityError.getText());
                        btUpdate.setEnabled(isValid);
                    } else {
                        btUpdate.setEnabled(false);
                    }
                }
            }
        });
    }

    private void initialiseAutoCompleteTextView() {
        productSearchArrayAdapter = new ProductSearchArrayAdapter(this, new ArrayList());
        atvSearch.setAdapter(productSearchArrayAdapter);
        atvSearch.setOnItemClickListener(this);
        atvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 2){
                    List<StockProductEntity> list = viewModel.sellebleProductsWithName(s.toString());
                    productSearchArrayAdapter.updateData(list);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void initialiseRecyclerView() {
        cashSalesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cashSalesRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        cashSalesListAdapter = new OrderItemsListAdapter(viewModel.getScannedProducts());
        cashSalesListAdapter.setOrderItemClickEventListener(deliveryOrderItemEventListener);
        cashSalesRecyclerView.setAdapter(cashSalesListAdapter);
    }

    private void onBarcodeDetected(String barcode) {
        StockProductEntity stockProduct = viewModel.sellebleProductHavingBarcode(barcode);
        if (stockProduct == null) {
            Toast.makeText(CashSalesSelectProductsActivity.this, R.string.product_not_available, Toast.LENGTH_SHORT).show();
            return;
        }
        addProductToSelectedList(stockProduct);
    }

    private void addProductToSelectedList(StockProductEntity stockProduct){
        if (viewModel.isProductInScannedList(stockProduct.getId())) {
            Toast.makeText(CashSalesSelectProductsActivity.this, R.string.item_already_added, Toast.LENGTH_SHORT).show();
        } else {
            viewModel.addToProductMaxQuantities(stockProduct.getId(), stockProduct.getQuantity(AppConstants.TYPE_SELLABLE));
            displayUpdateQuantityView(stockProduct);
        }
    }

    private void displayUpdateQuantityView(StockProductEntity stockProduct) {
        if (stockProduct != null) {
            viewModel.setSelectedStockProductEntity(stockProduct);
            etUnits.requestFocus();
            tvProductName.setText(stockProduct.getProductName() + " " + stockProduct.getWeight() + stockProduct.getWeightUnit());
            etUnits.setText("");
            etUnits.setHint(String.valueOf(viewModel.getMaxQuantityofProduct(stockProduct.getId())));
            updateQuantityLayout.setVisibility(View.VISIBLE);
            invalidQuantityError.setVisibility(View.INVISIBLE);
            btUpdate.setEnabled(false);
            AppUtils.toggleKeyboard(etUnits, getApplicationContext());
        }
    }

    private void hideUpdateQuantityView() {
        updateQuantityLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                //TODO: hide keyboard
                //AppUtils.hideKeyboard();
                finish();
                break;

            case R.id.bt_update:
                onUpdateButtonTap();
                break;
        }
    }

    private void onUpdateButtonTap(){
        StockProductEntity product = viewModel.getSelectedStockProductEntity();
        if (product != null) {
            product.setSellableQuantity(Integer.valueOf(etUnits.getText().toString()));
            if (!viewModel.isProductInScannedList(product.getId()))
                viewModel.addToScannedProduct(product);
            cashSalesListAdapter.notifyDataSetChanged();
            hideUpdateQuantityView();
            AppUtils.toggleKeyboard(etUnits, getApplicationContext());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        StockProductEntity product = (StockProductEntity) parent.getAdapter().getItem(position);
        if (product == null) return;
        addProductToSelectedList(product);
    }
}
