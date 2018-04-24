package com.goleep.driverapp.fragments;

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

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.WarehouseListAdapter;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.leep.dropoff.dropoff.DropoffActivity;
import com.goleep.driverapp.leep.dropoff.dropoff.DropoffWarehouseActivity;
import com.goleep.driverapp.leep.pickup.pickup.PickupActivity;
import com.goleep.driverapp.leep.pickup.pickup.PickupWarehouseActivity;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;
import com.goleep.driverapp.viewmodels.WarehouseViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 23/03/18.
 */

public class WarehouseListFragment extends Fragment {

    @BindView(R.id.warehouse_recycler_view)
    RecyclerView recyclerView;

    private WarehouseListAdapter adapter;
    private WarehouseViewModel viewModel;

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
        viewModel = ViewModelProviders.of(getActivity()).get(WarehouseViewModel.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new WarehouseListAdapter(viewModel.getWarehouses());
        adapter.setWarehouseSelectionListener(warehouseSelectionListener);
        recyclerView.setAdapter(adapter);
    }

    private void startPickupActivity(WarehouseEntity warehouseEntity) {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;
        String activityName = getActivity().getClass().getSimpleName();
        Intent intent = null;
        if (activityName.equals(PickupWarehouseActivity.class.getSimpleName())) intent = new Intent(activity, PickupActivity.class);
        else if (activityName.equals(DropoffWarehouseActivity.class.getSimpleName())) intent = new Intent(activity, DropoffActivity.class);
        Objects.requireNonNull(intent).putExtra(IntentConstants.WAREHOUSE_ID, warehouseEntity.getId());
        startActivityForResult(intent, 101);
    }
}
