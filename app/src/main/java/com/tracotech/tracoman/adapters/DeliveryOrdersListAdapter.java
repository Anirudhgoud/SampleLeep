package com.tracotech.tracoman.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.SortCategoryType;
import com.tracotech.tracoman.helpers.uimodels.BaseListItem;
import com.tracotech.tracoman.interfaces.DeliveryOrderClickEventListener;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.comparators.SortDOByDeliveryTime;
import com.tracotech.tracoman.services.room.entities.comparators.SortDoByPreferredDeliveryDate;
import com.tracotech.tracoman.services.room.entities.comparators.SortDoByTotalValue;
import com.tracotech.tracoman.viewholders.DeliveryOrdersViewHolder;

import java.util.Collections;
import java.util.List;

/**
 * Created by anurag on 16/02/18.
 */

public class DeliveryOrdersListAdapter extends RecyclerView.Adapter<DeliveryOrdersViewHolder> {

    private List<BaseListItem> deliveryOrderList;
    private DeliveryOrderClickEventListener deliveryOrderClickEventListener;

    public DeliveryOrdersListAdapter(List<BaseListItem> deliveryOrderList){
        this.deliveryOrderList = deliveryOrderList;
    }

    public void setDeliveryOrderClickEventListener(DeliveryOrderClickEventListener deliveryOrderClickEventListener){
        this.deliveryOrderClickEventListener = deliveryOrderClickEventListener;
    }

    @Override
    public DeliveryOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DeliveryOrdersViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivery_order_item, parent, false), deliveryOrderClickEventListener);
    }

    @Override
    public void onBindViewHolder(DeliveryOrdersViewHolder holder, int position) {
        DeliveryOrderEntity deliveryOrder = (DeliveryOrderEntity)deliveryOrderList.get(position);
        holder.bindData(deliveryOrder);
    }

    @Override
    public int getItemCount() {
        return deliveryOrderList.size();
    }

    public void updateList(List<BaseListItem> deliveryOrderList){
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

    public List<BaseListItem> getDeliveryOrderList() {
        return deliveryOrderList;
    }
}
