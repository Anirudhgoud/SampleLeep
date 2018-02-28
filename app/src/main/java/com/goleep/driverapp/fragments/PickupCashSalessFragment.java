package com.goleep.driverapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.PickupCashSalesListAdapter;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.interfaces.DoSelectionListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.ParentAppCompatActivity;
import com.goleep.driverapp.leep.PickupActivity;
import com.goleep.driverapp.leep.PickupConfirmationActivity;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.DoDetails;
import com.goleep.driverapp.viewmodels.CashSalesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 19/02/18.
 */

public class PickupCashSalessFragment extends Fragment{
    @BindView(R.id.cash_sales_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.confirm_button)
    CustomButton confirmButton;
    private CashSalesViewModel cashSalesViewModel;
    private PickupCashSalesListAdapter adapter;

    private UILevelNetworkCallback driverDoCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if(toLogout)
                ((PickupActivity)getActivity()).logoutUser();
        }
    };
    private UILevelNetworkCallback driverDoDetailsCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if(toLogout)
                ((PickupActivity)getActivity()).logoutUser();
        }
    };

    private DoSelectionListener doSelectionListener = new DoSelectionListener() {
        @Override
        public void allDOSelected(boolean allSelected) {
            if(allSelected)
                confirmButton.setVisibility(View.VISIBLE);
            else confirmButton.setVisibility(View.GONE);
        }
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
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PickupConfirmationActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initialiseRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new PickupCashSalesListAdapter(new ArrayList<DeliveryOrderItem>());
        recyclerView.setAdapter(adapter);
        cashSalesViewModel.getDriverDo().observe(PickupCashSalessFragment.this, new Observer<DeliveryOrder>() {
            @Override
            public void onChanged(@Nullable DeliveryOrder doDetails) {
                if(doDetails != null)
                    fetchDriverDoDetails(doDetails.getId());
            }
        });
    }

    private void fetchDriverDoDetails(Integer id) {
        cashSalesViewModel.getDriverDoDetails().observe(PickupCashSalessFragment.this, new Observer<DoDetails>() {
            @Override
            public void onChanged(@Nullable DoDetails doDetails) {
                if(doDetails != null)
                    adapter.updateList(doDetails.getDeliveryOrderItems());
            }
        });
        cashSalesViewModel.fetchDriverDoDetails(String.valueOf(id), driverDoDetailsCallback);
    }
}
