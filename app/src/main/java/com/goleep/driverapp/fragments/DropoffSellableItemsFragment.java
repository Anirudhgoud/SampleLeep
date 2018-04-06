package com.goleep.driverapp.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.OrderItemsListAdapter;
import com.goleep.driverapp.adapters.ProductListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomEditText;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.leep.DropoffActivity;
import com.goleep.driverapp.leep.WarehouseDropoffConfirmationActivity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.viewmodels.StocksViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 30/03/18.
 */

public class DropoffSellableItemsFragment extends Fragment {
    private StocksViewModel stocksViewModel;
    private OrderItemsListAdapter adapter;
    @BindView(R.id.stocks_recyclerview)
    RecyclerView stocksRecyclerView;
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
    @BindView(R.id.confirm_button)
    CustomButton confirmButton;
    private DeliveryOrderItemEventListener deliveryOrderItemEventListener = new DeliveryOrderItemEventListener() {
        @Override
        public void onUnitsTap(int itemId, int maxUnits) {
            displayUpdateQuantityView(itemId, maxUnits);
        }

        @Override
        public void onCheckboxTap(int itemId, boolean isChecked) {
            stocksViewModel.updateOrderItemSelectionStatus(itemId, isChecked);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dropoff_list, container, false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {
        stocksViewModel = ViewModelProviders.of(this).get(StocksViewModel.class);
        initRecyclerView();
        initialiseUpdateQuantityView();
        initilizeListeners();
    }

    private void initilizeListeners() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null && !getActivity().isFinishing()) {
                    startConfirmationActivity();
                }
            }
        });
    }

    private void startConfirmationActivity() {
        DropoffActivity activity = ((DropoffActivity) getActivity());
        if(activity != null && !activity.isFinishing()) {
            Intent intent = new Intent(activity, WarehouseDropoffConfirmationActivity.class);
            int warehouseId = getArguments().getInt(IntentConstants.WAREHOUSE_ID);
            intent.putExtra(IntentConstants.WAREHOUSE_ID, warehouseId);
            if (activity.getSelectedReturnableIds().size() > 0) {
                intent.putIntegerArrayListExtra(IntentConstants.RETURNABLE,
                        activity.getSelectedReturnableIds());
            }
            if (stocksViewModel.getSelectedIds().size() > 0) {
                intent.putIntegerArrayListExtra(IntentConstants.SELLABLE,
                        (ArrayList<Integer>) stocksViewModel.getSelectedIds());
            }
            startActivityForResult(intent, 101);
        }
    }

    private void initRecyclerView() {
        if(getActivity() != null && !getActivity().isFinishing()) {
            stocksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            stocksRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));
            adapter = new OrderItemsListAdapter(stocksViewModel.getStockList(
                    ProductListAdapter.TYPE_SELLABLE), ProductListAdapter.TYPE_SELLABLE);
            adapter.setOrderItemClickEventListener(deliveryOrderItemEventListener);
            stocksRecyclerView.setAdapter(adapter);
        }
    }

    private void initialiseUpdateQuantityView(){
        etUnits.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {
            @Override
            public void onDoneButtonPress() {
                hideUpdateQuantityView();
            }
        });
        etUnits.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                hideUpdateQuantityView();
                AppUtils.toggleKeyboard(etUnits, getContext().getApplicationContext());
            }
            return true;
        });
        etUnits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (stocksViewModel.getSelectedOrderItem() != null) {
                    int maxUnits = stocksViewModel.getSelectedOrderItem().
                            getMaxQuantity(ProductListAdapter.TYPE_SELLABLE);
                    String newUnitsText = etUnits.getText().toString();
                    if(newUnitsText.length() > 0){
                        int newUnits = Integer.valueOf(newUnitsText);
                        boolean isValid = newUnits <= maxUnits && newUnits != 0;
                        invalidQuantityError.setVisibility(isValid ? View.INVISIBLE : View.VISIBLE);
                        btUpdate.setEnabled(isValid);
                    }else{
                        btUpdate.setEnabled(false);
                    }
                }
            }
        });

        btUpdate.setOnClickListener(v -> {
            if (stocksViewModel.getSelectedOrderItem() != null) {
                stocksViewModel.updateOrderItemQuantity(stocksViewModel.getSelectedOrderItem().getId(),
                        Integer.valueOf(etUnits.getText().toString()), ProductListAdapter.TYPE_SELLABLE);
                adapter.updateList(stocksViewModel.getStockList(ProductListAdapter.TYPE_SELLABLE));
                hideUpdateQuantityView();
                AppUtils.toggleKeyboard(etUnits, getContext().getApplicationContext());
            }
        });
    }

    private void hideUpdateQuantityView() {
        updateQuantityLayout.setVisibility(View.GONE);
        confirmButton.setVisibility(View.VISIBLE);
    }

    private void displayUpdateQuantityView(int itemId, int currentUnits){
        etUnits.requestFocus();
        confirmButton.setVisibility(View.GONE);
        StockProductEntity selectedOrderItem = stocksViewModel.getStockProduct(itemId);
        stocksViewModel.setSelectedOrderItem(selectedOrderItem);
        if(selectedOrderItem != null ){
            tvProductName.setText(selectedOrderItem.getProductName() + " " + selectedOrderItem.getWeight()
                    + selectedOrderItem.getWeightUnit());
            etUnits.setText("");
            etUnits.setHint(String.valueOf(currentUnits));
            updateQuantityLayout.setVisibility(View.VISIBLE);
            invalidQuantityError.setVisibility(View.INVISIBLE);
            btUpdate.setEnabled(false);
            AppUtils.toggleKeyboard(etUnits, getContext().getApplicationContext());
        }
    }
}
