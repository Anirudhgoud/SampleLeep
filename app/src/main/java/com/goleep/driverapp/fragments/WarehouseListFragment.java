package com.goleep.driverapp.fragments;

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
import com.goleep.driverapp.adapters.WarehouseListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.leep.DropoffActivity;
import com.goleep.driverapp.leep.DropoffWarehouseActivity;
import com.goleep.driverapp.leep.PickupWarehouseActivity;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.PickupActivity;
import com.goleep.driverapp.viewmodels.WarehouseViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 23/03/18.
 */

public class WarehouseListFragment extends Fragment{
    private WarehouseListAdapter adapter;
    private WarehouseViewModel deliveryOrderViewModel;
    @BindView(R.id.warehouse_recycler_view)
    RecyclerView recyclerView;
    private UILevelNetworkCallback deliveryOrdersUILevelCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {

        }
    };

    private View.OnClickListener warehouseSelectionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         startPickupActivity(adapter.getItem((Integer) view.getTag()));
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warehouse_list, container, false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {
        deliveryOrderViewModel = ViewModelProviders.of(getActivity()).get(WarehouseViewModel.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new WarehouseListAdapter(deliveryOrderViewModel.getWarehouses());
        adapter.setWarehouseSelectionListener(warehouseSelectionListener);
        recyclerView.setAdapter(adapter);
    }

    private void startPickupActivity(WarehouseEntity warehouseEntity) {
        if(getActivity() != null && !getActivity().isFinishing()) {
            if(getActivity().getClass().getSimpleName().equals(PickupWarehouseActivity.class.getSimpleName())) {
                Intent intent = new Intent(getActivity(), PickupActivity.class);
                intent.putExtra(IntentConstants.WAREHOUSE_ID, warehouseEntity.getId());
                startActivityForResult(intent, 101);
            } else if(getActivity().getClass().getSimpleName().equals(DropoffWarehouseActivity.class.getSimpleName())){
                Intent intent = new Intent(getActivity(), DropoffActivity.class);
                intent.putExtra(IntentConstants.WAREHOUSE_ID, warehouseEntity.getId());
                startActivityForResult(intent, 101);
            }
        }
    }
}
