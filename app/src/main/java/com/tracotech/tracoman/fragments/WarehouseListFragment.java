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
import android.widget.Toast;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.WarehouseListAdapter;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.leep.dropoff.dropoff.DropoffActivity;
import com.tracotech.tracoman.leep.dropoff.dropoff.DropoffWarehouseActivity;
import com.tracotech.tracoman.leep.pickup.pickup.PickupActivity;
import com.tracotech.tracoman.leep.pickup.pickup.PickupWarehouseActivity;
import com.tracotech.tracoman.services.room.entities.WarehouseEntity;
import com.tracotech.tracoman.viewmodels.WarehouseViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
        FragmentActivity activity = getActivity();
        if(activity != null && !activity.isFinishing()) {
            viewModel = ViewModelProviders.of(activity).get(WarehouseViewModel.class);
            initRecyclerView();
        }
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        boolean isPickup = false;
        Bundle args = getArguments();
        if(args != null)
            isPickup = args.getBoolean(IntentConstants.IS_PICKUP);
        adapter = new WarehouseListAdapter(groupWarehouses(viewModel.getWarehouses()));
        adapter.setShowDoCount(isPickup);
        adapter.setWarehouseSelectionListener(warehouseSelectionListener);
        recyclerView.setAdapter(adapter);
    }

    private List<WarehouseEntity> groupWarehouses(List<WarehouseEntity> warehouses) {
        Collections.sort(warehouses, (w1, w2) -> w2.getDoAssignedCount() - w1.getDoAssignedCount());
        return warehouses;
    }

    private void startPickupActivity(WarehouseEntity warehouseEntity) {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isFinishing()) return;

        if (activity instanceof PickupWarehouseActivity && warehouseEntity.getDoAssignedCount() > 0) {
            Intent intent = new Intent(activity, PickupActivity.class);
            intent.putExtra(IntentConstants.WAREHOUSE_ID, warehouseEntity.getId());
            startActivityForResult(intent, 101);
        } else if(activity instanceof PickupWarehouseActivity && warehouseEntity.getDoAssignedCount() <= 0){
            Toast.makeText(getActivity(), getActivity().getResources().
                    getString(R.string.no_do_assigned), Toast.LENGTH_LONG).show();
        } else if (activity instanceof DropoffWarehouseActivity){
            Intent intent = new Intent(activity, DropoffActivity.class);
            intent.putExtra(IntentConstants.WAREHOUSE_ID, warehouseEntity.getId());
            startActivityForResult(intent, 101);
        }
    }
}
