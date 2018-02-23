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
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.DoExpandableListAdapter;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.PickupActivity;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.services.room.entities.DoDetails;
import com.goleep.driverapp.viewmodels.DeliveryOrdersViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 19/02/18.
 */

public class PickupDeliveryOrderFragment extends Fragment{

    private DeliveryOrdersViewModel doViewModel;

    @BindView(R.id.expandable_list)
    RecyclerView expandableListView;
    @BindView(R.id.confirm_button)
    CustomButton confirmButton;

    DoExpandableListAdapter adapter;
    ArrayList<DeliveryOrder> doList = new ArrayList<>();
    View.OnClickListener headerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int pos = expandableListView.getChildLayoutPosition(view);
            doViewModel.getDoDetails(adapter.getItemAt(pos).getId()).observe(
                    PickupDeliveryOrderFragment.this, new Observer<DoDetails>() {
                @Override
                public void onChanged(@Nullable DoDetails doDetails) {
                    adapter.updateItems(doDetails, pos);
                }
            });
            doViewModel.fetchDoItems(String.valueOf(adapter.getItemAt(pos).getId()));
        }
    };

    private UILevelNetworkCallback deliveryOrderCallBack = new UILevelNetworkCallback() {

        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup_do, container, false);
        ButterKnife.bind(this, view);
        initialise();
        fetchDeliveryOrders();
        return view;
    }

    private void initialise() {
        doViewModel = ViewModelProviders.of(getActivity()).get(DeliveryOrdersViewModel.class);
        initRecyclerView();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PickupActivity)getActivity()).viewPager.setCurrentItem(1);
            }
        });
    }

    private void initRecyclerView() {
        expandableListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        expandableListView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapter = new DoExpandableListAdapter(getActivity(), doList, headerClickListener);
        expandableListView.setAdapter(adapter);
        doViewModel.getDeliveryOrders(DeliveryOrdersViewModel.TYPE_CUSTOMER,
                DeliveryOrdersViewModel.STATUS_ASSIGNED).observe(
                        PickupDeliveryOrderFragment.this, new Observer<List<DeliveryOrder>>() {
            @Override
            public void onChanged(@Nullable List<DeliveryOrder> deliveryOrders) {
                doList.clear();
                doList.addAll(deliveryOrders);
                adapter.upDateList(deliveryOrders);
            }
        });
    }

    private void fetchDeliveryOrders() {
        doViewModel.fetchAllDeliveryOrders(deliveryOrderCallBack);
    }
}
