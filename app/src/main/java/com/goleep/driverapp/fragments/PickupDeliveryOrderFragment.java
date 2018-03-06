package com.goleep.driverapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.adapters.DoExpandableListAdapter;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.interfaces.ItemCheckListener;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.PickupActivity;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.viewmodels.DropOffDeliveryOrdersViewModel;
import com.goleep.driverapp.viewmodels.PickupDeliveryOrderViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vishalm on 19/02/18.
 */

public class PickupDeliveryOrderFragment extends Fragment{

    private PickupDeliveryOrderViewModel doViewModel;

    @BindView(R.id.expandable_list)
    RecyclerView expandableListView;
    @BindView(R.id.confirm_button)
    CustomButton confirmButton;

    private DoExpandableListAdapter adapter;
    private List<BaseListItem> doList = new ArrayList<>();
    private Map<Integer, Boolean> doUpdateMap = new HashMap<>();
    private ItemCheckListener itemCheckListener;
    private View.OnClickListener headerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int pos = expandableListView.getChildLayoutPosition(view);
            if(doUpdateMap.containsKey(((DeliveryOrderEntity)adapter.getItemAt(pos)).getId()) &&
                    !doUpdateMap.get(((DeliveryOrderEntity)adapter.getItemAt(pos)).getId())) {
                doViewModel.getDoDetails(((DeliveryOrderEntity)adapter.getItemAt(pos)).getId()).observe(
                        PickupDeliveryOrderFragment.this, new Observer<List<DeliveryOrderItem>>() {
                            @Override
                            public void onChanged(@Nullable List<DeliveryOrderItem> doDetails) {
                                List<BaseListItem> listItems = new ArrayList<>();
                                listItems.addAll(doDetails);
                                adapter.addItemsList(listItems, pos);
                                doUpdateMap.put(((DeliveryOrderEntity)adapter.getItemAt(pos)).getId(), true);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        expandableListView.findViewHolderForAdapterPosition(pos).itemView.performClick();
                                    }
                                }, 5);

                            }
                        });
            } else if(!doUpdateMap.containsKey(((DeliveryOrderEntity)adapter.getItemAt(pos)).getId())) {
                doViewModel.fetchDoItems(((DeliveryOrderEntity)adapter.getItemAt(pos)).getId());
            }
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
        doViewModel = ViewModelProviders.of(getActivity()).get(PickupDeliveryOrderViewModel.class);
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
        adapter = new DoExpandableListAdapter(getActivity(), doList);
        adapter.setHeaderClickListener(headerClickListener);
        expandableListView.setAdapter(adapter);
        doViewModel.getDeliveryOrders(DropOffDeliveryOrdersViewModel.TYPE_CUSTOMER,
                DropOffDeliveryOrdersViewModel.STATUS_ASSIGNED).observe(
                        PickupDeliveryOrderFragment.this, new Observer<List<DeliveryOrderEntity>>() {
            @Override
            public void onChanged(@Nullable List<DeliveryOrderEntity> deliveryOrders) {
                if(deliveryOrders.size() > 0) {
                    doList.clear();
                    doList.addAll(deliveryOrders);
                    List<BaseListItem> baseListItems = new ArrayList<>();
                    baseListItems.addAll(deliveryOrders);
                    adapter.upDateList(baseListItems);
                }
            }
        });
    }

    private void fetchDeliveryOrders() {
        doViewModel.fetchAllDeliveryOrders(deliveryOrderCallBack);
    }

    public void setItemSelectionListener(ItemCheckListener itemSelectionListener) {
        this.itemCheckListener = itemSelectionListener;
    }
}
