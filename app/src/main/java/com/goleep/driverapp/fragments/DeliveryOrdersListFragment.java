package com.goleep.driverapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_order_list, container, false);
        final DeliveryOrdersViewModel doViewModel = ViewModelProviders.of(getActivity()).get(DeliveryOrdersViewModel.class);
        RecyclerView doListRecyclerView = view.findViewById(R.id.delivery_order_recyclerview);
        doListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final DeliveryOrdersListAdapter doListAdapter = new DeliveryOrdersListAdapter(new ArrayList<DeliveryOrder>());
        doListRecyclerView.setAdapter(doListAdapter);

        doViewModel.deliveryOrders.observe(DeliveryOrdersListFragment.this, new Observer<List<DeliveryOrder>>() {
            @Override
            public void onChanged(@Nullable List<DeliveryOrder> deliveryOrders) {
                doListAdapter.updateList(deliveryOrders);
            }
        });

        doViewModel.fetchAllDeliveryOrders(loginCallBack);

        return view;
    }

    private UILevelNetworkCallback loginCallBack = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage) {

        }
    };
}
