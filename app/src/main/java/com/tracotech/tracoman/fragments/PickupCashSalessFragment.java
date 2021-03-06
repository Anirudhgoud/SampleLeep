package com.tracotech.tracoman.fragments;

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
import android.widget.TextView;
import android.widget.Toast;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.PickupCashSalesListAdapter;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.interfaces.ItemCheckListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.leep.pickup.pickup.PickupActivity;
import com.tracotech.tracoman.leep.pickup.pickup.PickupConfirmationActivity;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.viewmodels.dropoff.cashsales.CashSalesViewModel;

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
    @BindView(R.id.tv_cash_sales_empty)
    TextView tvEmptyList;
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
        adapter = new PickupCashSalesListAdapter(new ArrayList<>());
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
            }else {
                Toast.makeText(activity, activity.getString(R.string.no_item_selected), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onChanged(@Nullable Object object) {
        if(object instanceof DeliveryOrderEntity){
            fetchDriverDoDetails(((DeliveryOrderEntity)object).getId());
        } else if(object instanceof List){
            List<OrderItemEntity> orderItemEntities = (List<OrderItemEntity>)object;
            displayList(orderItemEntities);
        }

    }

    public void setItemSelectionListener(ItemCheckListener itemCheckListener) {
        this.itemCheckListener = itemCheckListener;
    }

    private void displayList(List<OrderItemEntity> orderItemEntities){
        if(orderItemEntities.size() > 0){
            tvEmptyList.setVisibility(View.GONE);
            adapter.updateList(orderItemEntities);
        } else {
            tvEmptyList.setVisibility(View.VISIBLE);
        }
    }

}
