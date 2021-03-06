package com.tracotech.tracoman.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;

import android.os.Handler;

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

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.adapters.DoExpandableListAdapter;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.constants.IntentConstants;
import com.tracotech.tracoman.helpers.uimodels.BaseListItem;
import com.tracotech.tracoman.interfaces.ItemCheckListener;
import com.tracotech.tracoman.interfaces.UILevelNetworkCallback;
import com.tracotech.tracoman.leep.pickup.pickup.PickupActivity;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.viewmodels.dropoff.deliveryorders.DropOffDeliveryOrdersViewModel;
import com.tracotech.tracoman.viewmodels.pickup.pickup.PickupDeliveryOrderViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 19/02/18.
 */

public class PickupDeliveryOrderFragment extends Fragment implements Observer<List<OrderItemEntity>> {

    private PickupDeliveryOrderViewModel doViewModel;

    @BindView(R.id.expandable_list)
    RecyclerView expandableListView;
    @BindView(R.id.confirm_button)
    Button confirmButton;

    private ItemCheckListener itemCheckListener;
    private DoExpandableListAdapter adapter;
    private DialogInterface.OnClickListener dialogPositiveClickListener = (dialog, which) -> {
        PickupActivity activity = (PickupActivity) getActivity();
        if(activity != null && !activity.isFinishing())
        (activity).viewPager.setCurrentItem(1);
    };

    private DialogInterface.OnClickListener dialogNegativeClickListener = (dialog, which) -> dialog.dismiss();

    private View.OnClickListener headerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int pos = expandableListView.getChildLayoutPosition(view);
            int doId = ((DeliveryOrderEntity)adapter.getItemAt(pos)).getId();
            doViewModel.getPositionMap().put(doId, pos);
            if(!doViewModel.getDoUpdateMap().containsKey(doId)) {
                doViewModel.fetchDoItems(doId, PickupDeliveryOrderFragment.this);
            }
        }
    };

    private UILevelNetworkCallback deliveryOrderCallBack = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> { };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickup_do, container, false);
        ButterKnife.bind(this, view);
        initialise();
        fetchDeliveryOrders();
        return view;
    }

    private void initialise() {
        doViewModel = ViewModelProviders.of(getActivity()).get(PickupDeliveryOrderViewModel.class);
        doViewModel.setWarehouse(getArguments().getInt(IntentConstants.WAREHOUSE_ID, -1));
        initRecyclerView();
        confirmButton.setOnClickListener(v -> {
            FragmentActivity activity = getActivity();
            if(activity != null && !activity.isFinishing())
                if(adapter.isPartialDoSelected()){
                    ((PickupActivity)activity).showConfirmationDialog(getString(R.string.confirmation),
                            getString(R.string.partial_do_confirmation), dialogPositiveClickListener, dialogNegativeClickListener);
                } else {
                    adapter.collapseAllExcept(-1);
                    ((PickupActivity)activity).viewPager.setCurrentItem(1);
                }
        });
    }

    private void initRecyclerView() {
        expandableListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        expandableListView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapter = new DoExpandableListAdapter(getActivity(), doViewModel.getDoList());
        adapter.setItemCheckListener(itemCheckListener);
        adapter.setHeaderClickListener(headerClickListener);
        expandableListView.setAdapter(adapter);
        doViewModel.getDeliveryOrders(DropOffDeliveryOrdersViewModel.TYPE_CUSTOMER,
                DropOffDeliveryOrdersViewModel.STATUS_ASSIGNED, doViewModel.getWarehouse().getId()).observe(
                        PickupDeliveryOrderFragment.this, deliveryOrders -> {
                            if(deliveryOrders.size() > 0) {
                                doViewModel.getDoList().clear();
                                for(DeliveryOrderEntity deliveryOrderEntity : deliveryOrders) {
                                    deliveryOrderEntity.setItemType(AppConstants.TYPE_HEADER);
                                    doViewModel.getDoList().add(deliveryOrderEntity);
                                }
                                List<BaseListItem> baseListItems = new ArrayList<>(deliveryOrders);
                                adapter.upDateList(baseListItems);
                            }
                        });
        doViewModel.getOrderItemsLiveData().observe(PickupDeliveryOrderFragment.this,
                PickupDeliveryOrderFragment.this);
    }

    private void fetchDeliveryOrders() {
        int warehouseId = -1;
        if(doViewModel.getWarehouse() != null)
            warehouseId = doViewModel.getWarehouse().getId();
        doViewModel.fetchAllDeliveryOrders(deliveryOrderCallBack, null, null,
                null, warehouseId, null);
    }

    public void setItemSelectionListener(ItemCheckListener itemSelectionListener) {
        this.itemCheckListener = itemSelectionListener;
    }

    @Override
    public void onChanged(@Nullable List<OrderItemEntity> doDetails) {
        if (doDetails != null && doDetails.size() > 0 && doViewModel.getPositionMap().
                indexOfKey(doDetails.get(0).getOrderId()) >= 0) {
            final int doId = doDetails.get(0).getOrderId();
            List<BaseListItem> listItems = new ArrayList<>();
            for(OrderItemEntity orderItemEntity : doDetails){
                orderItemEntity.setItemType(AppConstants.TYPE_DO_ITEM);
                listItems.add(orderItemEntity);
            }
            if(getActivity() != null && !getActivity().isFinishing())
                getActivity().runOnUiThread(() -> {
                    adapter.addItemsList(listItems, doId);
                    doViewModel.getDoUpdateMap().put(doId, true);
                    new Handler().postDelayed(() -> {
                        int position = doViewModel.getPositionMap().
                                indexOfKey(doDetails.get(0).getOrderId());
                        RecyclerView.ViewHolder viewHolder = expandableListView.
                                findViewHolderForAdapterPosition(position);
                        if(viewHolder != null && viewHolder.itemView != null) {
                            viewHolder.itemView.performClick();
                            expandableListView.scrollToPosition(position);
                        }
                    },10);
                });
        }
    }
}
