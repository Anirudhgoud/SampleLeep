package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;
import com.goleep.driverapp.viewholders.WarehouseListViewHolder;

import java.util.List;

/**
 * Created by vishalm on 28/03/18.
 */

public class WarehouseListAdapter extends RecyclerView.Adapter<WarehouseListViewHolder> {
    private List<WarehouseEntity> warehouseList;
    private View.OnClickListener warehouseSelectionListener;
    private boolean isPickup;

    public WarehouseListAdapter(List<WarehouseEntity> warehouseList, boolean isPickup) {
        this.warehouseList = warehouseList;
        this.isPickup = isPickup;
    }

    public void setWarehouseSelectionListener(View.OnClickListener warehouseSelectionListener) {
        this.warehouseSelectionListener = warehouseSelectionListener;
    }


    @Override
    public WarehouseListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WarehouseListViewHolder holder = new WarehouseListViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.warehouse_list_item, parent, false));
        holder.setWarehouseSelectionListener(warehouseSelectionListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(WarehouseListViewHolder holder, int position) {
        holder.bind(warehouseList.get(position), position, isPickup);
    }

    @Override
    public int getItemCount() {
        return warehouseList.size();
    }

//    public void updateList(List<WarehouseEntity> warehouseEntities) {
//        this.warehouseList = warehouseEntities;
//        notifyDataSetChanged();
//    }

    public WarehouseEntity getItem(int position) {
        return warehouseList.get(position);
    }
}
