package com.tracotech.tracoman.fragments;

import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.OrderItemsListAdapter;
import com.tracotech.tracoman.adapters.ProductListAdapter;
import com.tracotech.tracoman.helpers.customviews.CustomEditText;
import com.tracotech.tracoman.interfaces.DeliveryOrderItemEventListener;
import com.tracotech.tracoman.leep.dropoff.dropoff.DropoffActivity;
import com.tracotech.tracoman.services.room.entities.StockProductEntity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.viewmodels.information.StocksViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 30/03/18.
 */

public class DropoffReturnedFragment extends Fragment {
    private StocksViewModel stocksViewModel;
    private OrderItemsListAdapter adapter;
    @BindView(R.id.stocks_recyclerview)
    RecyclerView stocksRecyclerView;
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
    @BindView(R.id.confirm_button)
    Button nextButton;
    private View.OnClickListener nextClickListener;

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
        initView();
    }

    private void initView() {
        nextButton.setText(getContext().getResources().getString(R.string.next));
        initRecyclerView();
        initialiseUpdateQuantityView();
        setClickListeners();
    }

    public void setNextClickListener(View.OnClickListener nextClickListener){
        this.nextClickListener = nextClickListener;
    }

    private void setClickListeners() {

        nextButton.setOnClickListener(view -> {
            nextClickListener.onClick(view);
        });
    }

    private void initRecyclerView() {
        if(getActivity() != null && !getActivity().isFinishing()) {
            stocksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            stocksRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));
            adapter = new OrderItemsListAdapter(stocksViewModel.getStockList(
                    ProductListAdapter.TYPE_RETURNED), ProductListAdapter.TYPE_RETURNED);
            adapter.setOrderItemClickEventListener(deliveryOrderItemEventListener);
            stocksRecyclerView.setAdapter(adapter);
            setInitData();
        }
    }

    private void setInitData() {
        if(getActivity() != null && !getActivity().isFinishing()){
            ((DropoffActivity)getActivity()).setSelectedReturnableIds(
                    (ArrayList<Integer>) stocksViewModel.getSelectedIds());
        }
    }


    private void initialiseUpdateQuantityView(){
        etUnits.setKeyImeChangeListener(this::hideUpdateQuantityView);
        etUnits.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                hideUpdateQuantityView();
                AppUtils.toggleKeyboard(etUnits);
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
                            getMaxQuantity(ProductListAdapter.TYPE_RETURNED);
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
                        Integer.valueOf(etUnits.getText().toString()), ProductListAdapter.TYPE_RETURNED);
                adapter.updateList(stocksViewModel.getStockList(ProductListAdapter.TYPE_RETURNED));
                hideUpdateQuantityView();
                AppUtils.toggleKeyboard(etUnits);
            }
        });
    }

    private void hideUpdateQuantityView() {
        updateQuantityLayout.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);
    }


    private void displayUpdateQuantityView(int itemId, int currentUnits){
        etUnits.requestFocus();
        nextButton.setVisibility(View.GONE);
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
            AppUtils.toggleKeyboard(etUnits);
        }
    }
}
