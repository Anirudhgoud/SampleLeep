package com.goleep.driverapp.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.PickupCashSalesListAdapter;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.interfaces.ItemCheckListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.leep.pickup.pickup.PickupActivity;
import com.goleep.driverapp.leep.pickup.pickup.PickupConfirmationActivity;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.viewmodels.dropoff.cashsales.CashSalesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 19/02/18.
 */

public class PickupCashSalessFragment extends Fragment implements View.OnClickListener, Observer{
    @BindView(R.id.cash_sales_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.confirm_button)
    Button confirmButton;
    private CashSalesViewModel cashSalesViewModel;
    private PickupCashSalesListAdapter adapter;
    private ItemCheckListener itemCheckListener;

    private UILevelNetworkCallback driverDoCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        ParentAppCompatActivity parentActivity = ((ParentAppCompatActivity) activity);
        parentActivity.runOnUiThread(() -> {
            if(toLogout) parentActivity.logoutUser();
        });
    };
    private UILevelNetworkCallback driverDoDetailsCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        ParentAppCompatActivity parentActivity = ((ParentAppCompatActivity) activity);
        parentActivity.runOnUiThread(() -> {
            if(toLogout) parentActivity.logoutUser();
        });
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup_cash_sales, container, false);
        ButterKnife.bind(this, view);
        initialize();
        initialiseRecyclerView();
        fetchDriverDo();
        return view;
    }

    private void fetchDriverDo() {
        cashSalesViewModel.fetchDriverDo(driverDoCallback);
    }

    private void initialize() {
        cashSalesViewModel = ViewModelProviders.of(getActivity()).get(CashSalesViewModel.class);
        cashSalesViewModel.setWarehouse(getArguments().getInt(IntentConstants.WAREHOUSE_ID, -1));
        confirmButton.setOnClickListener(PickupCashSalessFragment.this);
    }

    private void initialiseRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new PickupCashSalesListAdapter(new ArrayList<OrderItemEntity>());
        adapter.setItemCheckListener(itemCheckListener);
        recyclerView.setAdapter(adapter);
        cashSalesViewModel.getDriverDo().observe(PickupCashSalessFragment.this,
                PickupCashSalessFragment.this);
    }

    private void fetchDriverDoDetails(Integer id) {
        cashSalesViewModel.getDriverDoDetails(id).observe(PickupCashSalessFragment.this,
                PickupCashSalessFragment.this);
        cashSalesViewModel.fetchDriverDoDetails(id, driverDoDetailsCallback);
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.confirm_button : startConfirmActivity();
               break;
       }
    }

    private void startConfirmActivity() {
        Activity activity = getActivity();
        if(activity != null && !activity.isFinishing()) {
            if (((PickupActivity) getActivity()).getSelectedCashSalesIds().size() > 0 ||
                    ((PickupActivity) getActivity()).getSelectedDoIds().size() > 0) {
                Intent intent = new Intent(getActivity(), PickupConfirmationActivity.class);
                intent.putExtra(IntentConstants.WAREHOUSE_ID, cashSalesViewModel.getWarehouse().getId());
                intent.putIntegerArrayListExtra(AppConstants.CASH_DOITEM_KEY,
                        (ArrayList<Integer>) ((PickupActivity) getActivity()).getSelectedCashSalesIds());
                intent.putIntegerArrayListExtra(AppConstants.DO_IDS_KEY,
                        (ArrayList<Integer>) ((PickupActivity) getActivity()).getSelectedDoIds());
                getActivity().startActivityForResult(intent, 101);
            }
        }
    }

    @Override
    public void onChanged(@Nullable Object object) {
        if(object instanceof DeliveryOrderEntity){
            fetchDriverDoDetails(((DeliveryOrderEntity)object).getId());
        } else if(object instanceof List){
            List<OrderItemEntity> orderItemEntities = (List<OrderItemEntity>)object;
            if(orderItemEntities.size() > 0) {
                adapter.updateList(orderItemEntities);
            }
        }

    }

    public void setItemSelectionListener(ItemCheckListener itemCheckListener) {
        this.itemCheckListener = itemCheckListener;
    }
}
