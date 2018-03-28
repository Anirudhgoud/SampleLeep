package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.viewholders.HistoryListViewHolder;

import java.util.List;

/**
 * Created by vishalm on 20/03/18.
 */

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListViewHolder> {
    private List<DeliveryOrderEntity> deliveryOrderEntities;
    private View.OnClickListener detailsClickListener;

    public HistoryListAdapter(List<DeliveryOrderEntity> orderHistoryList) {
        this.deliveryOrderEntities = orderHistoryList;
    }

    public void setDetailsClickListener(View.OnClickListener detailsCliclListener) {
        this.detailsClickListener = detailsCliclListener;
    }

    @Override
    public HistoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HistoryListViewHolder viewHolder = new HistoryListViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.do_history_item, parent, false));
        viewHolder.setDetailsClickListener(detailsClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryListViewHolder holder, int position) {
        if (deliveryOrderEntities.size() > position) {
            holder.bind(deliveryOrderEntities.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return deliveryOrderEntities.size();
    }

    public void updateList(List<DeliveryOrderEntity> deliveryOrderEntities) {
        this.deliveryOrderEntities = deliveryOrderEntities;
        notifyDataSetChanged();
    }
}