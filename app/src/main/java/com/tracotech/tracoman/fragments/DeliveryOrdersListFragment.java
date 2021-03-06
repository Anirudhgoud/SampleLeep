package com.tracotech.tracoman.fragments;

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
import android.widget.RadioGroup;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.DeliveryOrdersListAdapter;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.constants.SortCategoryType;
import com.tracotech.tracoman.helpers.uimodels.BaseListItem;
import com.tracotech.tracoman.interfaces.DeliveryOrderClickEventListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.dropoff.deliveryorders.DropOffDeliveryOrderDetailsActivity;
import com.tracotech.tracoman.leep.main.ParentAppCompatActivity;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.viewmodels.dropoff.deliveryorders.DropOffDeliveryOrdersViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 15/02/18.
 */

public class DeliveryOrdersListFragment extends Fragment {

    private DropOffDeliveryOrdersViewModel doViewModel;
    private DeliveryOrdersListAdapter doListAdapter;

    //UI elements
    private RecyclerView doListRecyclerView;
    private RadioGroup rgFilterRadioGroup;

    private DeliveryOrderClickEventListener deliveryOrderClickEventListener = this::openDeliveryDetailsActivity;

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

    private void initialise() {
        FragmentActivity activity = getActivity();
        if (activity == null) return;
        doViewModel = ViewModelProviders.of(activity).get(DropOffDeliveryOrdersViewModel.class);
        initialiseRecyclerView();
        initialiseRadioButtons();
        observeDistanceChanges();
    }

    private void initialiseRecyclerView() {
        doListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        doListRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        doListAdapter = new DeliveryOrdersListAdapter(new ArrayList<>());
        doListAdapter.setDeliveryOrderClickEventListener(deliveryOrderClickEventListener);
        doViewModel.getDeliveryOrders(DropOffDeliveryOrdersViewModel.TYPE_CUSTOMER,
                DropOffDeliveryOrdersViewModel.STATUS_IN_TRANSIT).observe(
                DeliveryOrdersListFragment.this, deliveryOrders -> {
                    List<BaseListItem> baseListItems = new ArrayList<>(deliveryOrders);
                    doListAdapter.updateList(baseListItems);
                });
        doListRecyclerView.setAdapter(doListAdapter);
    }

    private void observeDistanceChanges() {
        doViewModel.getTimeToReachDistanceMatrix().observe(this, distances -> {
            List<DeliveryOrderEntity> deliveryOrders = doViewModel.getDeliveryOrders();
            List<BaseListItem> baseListItems = doViewModel.updatedOrders(deliveryOrders, distances);
            if (baseListItems != null)
                doListAdapter.updateList(baseListItems);
        });
    }

    private void initialiseRadioButtons() {
        rgFilterRadioGroup.setOnCheckedChangeListener((group, checkedId) -> onRadioSelectionChange(checkedId));
    }

    private void fetchDeliveryOrders() {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        ParentAppCompatActivity parentActivity = ((ParentAppCompatActivity) activity);
        parentActivity.showProgressDialog();
        doViewModel.fetchAllDeliveryOrders(deliveryOrderCallBack, null, null, null, -1, null);
    }

    private void onRadioSelectionChange(int checkedId) {
        switch (checkedId) {
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

    private void openDeliveryDetailsActivity(Integer deliveryOrderId) {
        Intent doDetailsIntent = new Intent(getActivity(), DropOffDeliveryOrderDetailsActivity.class);
        doDetailsIntent.putExtra(IntentConstants.DELIVERY_ORDER_ID, deliveryOrderId);
        startActivity(doDetailsIntent);
    }

    private UILevelNetworkCallback deliveryOrderCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        ParentAppCompatActivity activity = ((ParentAppCompatActivity) getActivity());
        if (activity == null) return;
        activity.dismissProgressDialog();
        activity.runOnUiThread(() -> {
            if (toLogout) {
                activity.logoutUser();
            } else if (isDialogToBeShown) {
                activity.showNetworkRelatedDialogs(errorMessage);
            }
        });

    };
}
