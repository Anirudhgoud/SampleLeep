package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.viewholders.DeliveryOrdersViewHolder;

import java.util.List;

/**
 * Created by anurag on 16/02/18.
 */

public class DeliveryOrdersListAdapter extends RecyclerView.Adapter<DeliveryOrdersViewHolder> {

    private List<DeliveryOrder> deliveryOrderList;

    public DeliveryOrdersListAdapter(List<DeliveryOrder> deliveryOrderList){
        this.deliveryOrderList = deliveryOrderList;
    }

    @Override
    public DeliveryOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeliveryOrdersViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DeliveryOrdersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return deliveryOrderList.size();
    }

    public void updateList(List<DeliveryOrder> deliveryOrderList){
        this.deliveryOrderList.clear();
        this.deliveryOrderList.addAll(deliveryOrderList);
        notifyDataSetChanged();
    }
}
