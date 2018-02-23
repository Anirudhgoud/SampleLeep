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
import android.widget.RadioGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.DeliveryOrdersListAdapter;
import com.goleep.driverapp.constants.SortCategoryType;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.viewmodels.DeliveryOrdersViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 15/02/18.
 */

public class DeliveryOrdersListFragment extends Fragment {

    private DeliveryOrdersViewModel doViewModel;
    private DeliveryOrdersListAdapter doListAdapter;

    //UI elements
    private RecyclerView doListRecyclerView;
    private RadioGroup rgFilterRadioGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_order_list, container, false);
        doListRecyclerView = view.findViewById(R.id.delivery_order_recyclerview);
        rgFilterRadioGroup = view.findViewById(R.id.rg_sort);
        initialise();
        fetchDeliveryOrders();
        return view;
    }

    private void initialise(){
        doViewModel = ViewModelProviders.of(getActivity()).get(DeliveryOrdersViewModel.class);
        initialiseRecyclerView();
        initialiseRadioButtons();
    }

    private void initialiseRecyclerView(){
        doListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        doListRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        doListAdapter = new DeliveryOrdersListAdapter(new ArrayList<DeliveryOrder>());
        doViewModel.getDeliveryOrders(DeliveryOrdersViewModel.TYPE_CUSTOMER,
                DeliveryOrdersViewModel.STATUS_IN_TRANSIT).observe(
                        DeliveryOrdersListFragment.this, new Observer<List<DeliveryOrder>>() {
            @Override
            public void onChanged(@Nullable List<DeliveryOrder> deliveryOrders) {
                doListAdapter.updateList(deliveryOrders);
            }
        });
        doListRecyclerView.setAdapter(doListAdapter);
    }

    private void initialiseRadioButtons(){
        rgFilterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioSelectionChange(checkedId);
            }
        });
    }

    private void fetchDeliveryOrders(){
        doViewModel.fetchAllDeliveryOrders(deliveryOrderCallBack);
    }

    private void onRadioSelectionChange(int checkedId){
        switch (checkedId){
            case R.id.rb_date:
                doListAdapter.sortList(SortCategoryType.DATE);
                break;

            case R.id.rb_distance:
                doListAdapter.sortList(SortCategoryType.DISTANCE);
                break;

            case R.id.rb_value:
                doListAdapter.sortList(SortCategoryType.VALUE);
                break;
        }
    }

    private UILevelNetworkCallback deliveryOrderCallBack = new UILevelNetworkCallback() {

        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {

        }
    };
}
