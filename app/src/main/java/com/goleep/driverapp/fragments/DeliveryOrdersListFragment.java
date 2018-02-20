package com.goleep.driverapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.goleep.driverapp.adapters.DeliveryOrdersListAdapter;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.viewmodels.DeliveryOrdersViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by anurag on 15/02/18.
 */

public class DeliveryOrdersListFragment extends Fragment {

    private DeliveryOrdersViewModel doViewModel;
    private RecyclerView doListRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_order_list, container, false);
        doListRecyclerView = view.findViewById(R.id.delivery_order_recyclerview);
        initialise();
        fetchDeliveryOrders();
        return view;
    }

    private void initialise(){
        doViewModel = ViewModelProviders.of(getActivity()).get(DeliveryOrdersViewModel.class);
        initialiseRecyclerView();
    }

    private void initialiseRecyclerView(){
        doListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        doListRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        final DeliveryOrdersListAdapter doListAdapter = new DeliveryOrdersListAdapter(new ArrayList<DeliveryOrder>());
        doViewModel.getDeliveryOrders().observe(DeliveryOrdersListFragment.this, new Observer<List<DeliveryOrder>>() {
            @Override
            public void onChanged(@Nullable List<DeliveryOrder> deliveryOrders) {
                doListAdapter.updateList(deliveryOrders);
            }
        });
        doListRecyclerView.setAdapter(doListAdapter);
    }

    private void fetchDeliveryOrders(){
        doViewModel.fetchAllDeliveryOrders(deliveryOrderCallBack);
    }

    private UILevelNetworkCallback deliveryOrderCallBack = new UILevelNetworkCallback() {

        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {

        }
    };
}
