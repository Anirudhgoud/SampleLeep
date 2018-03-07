package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.viewholders.OrderItemsViewHolder;

import java.util.List;

/**
 * Created by anurag on 28/02/18.
 */

public class OrderItemsListAdapter extends RecyclerView.Adapter<OrderItemsViewHolder> {

    private List<OrderItemEntity> orderItemList;
    private DeliveryOrderItemEventListener deliveryOrderItemEventListener;

    public OrderItemsListAdapter(List<OrderItemEntity> orderItemList){
        this.orderItemList = orderItemList;
    }

    public void setOrderItemClickEventListener(DeliveryOrderItemEventListener deliveryOrderItemEventListener){
        this.deliveryOrderItemEventListener = deliveryOrderItemEventListener;
    }

    @Override
    public OrderItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderItemsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.do_details_list_item, parent, false), deliveryOrderItemEventListener);
    }

    @Override
    public void onBindViewHolder(OrderItemsViewHolder holder, int position) {
        OrderItemEntity orderItem = orderItemList.get(position);
        holder.bindData(orderItem);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public void updateList(List<OrderItemEntity> deliveryOrderList){
        this.orderItemList.clear();
        this.orderItemList.addAll(deliveryOrderList);
        notifyDataSetChanged();
    }
}
