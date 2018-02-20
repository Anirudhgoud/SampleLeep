package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.SortCategoryType;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.goleep.driverapp.services.room.entities.comparators.SortDOByDeliveryTime;
import com.goleep.driverapp.services.room.entities.comparators.SortDoByPreferredDeliveryDate;
import com.goleep.driverapp.services.room.entities.comparators.SortDoByTotalValue;
import com.goleep.driverapp.viewholders.DeliveryOrdersViewHolder;

import java.util.Collections;
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
        DeliveryOrder deliveryOrder = deliveryOrderList.get(position);
        holder.bindData(deliveryOrder);
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

    public void sortList(String sortType){
        switch (sortType){
            case SortCategoryType.DATE:
                Collections.sort(deliveryOrderList, new SortDoByPreferredDeliveryDate());
                break;

            case SortCategoryType.DISTANCE:
                Collections.sort(deliveryOrderList, new SortDOByDeliveryTime());
                break;

            case SortCategoryType.VALUE:
                Collections.sort(deliveryOrderList, new SortDoByTotalValue());
                break;
        }
        notifyDataSetChanged();
    }
}
