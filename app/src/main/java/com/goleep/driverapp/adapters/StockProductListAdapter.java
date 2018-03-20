package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.goleep.driverapp.R;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.viewholders.StocksListViewHolder;

import java.util.List;

/**
 * Created by vishalm on 20/03/18.
 */

public class StockProductListAdapter extends RecyclerView.Adapter<StocksListViewHolder> {

    private List<StockProductEntity> stockProductEntities;
    public static final int TYPE_DELIVERABLE = 0;
    public static final int TYPE_SELLABLE = 1;
    public static final int TYPE_RETURNED = 2;
    private int listType = TYPE_DELIVERABLE;

    public StockProductListAdapter(List<StockProductEntity> stockProducts){
        stockProductEntities = stockProducts;
    }

    @Override
    public StocksListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StocksListViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.do_details_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StocksListViewHolder holder, int position) {
        if(stockProductEntities.size() > 0)
            holder.bind(stockProductEntities.get(position), listType);
    }

    @Override
    public int getItemCount() {
        return stockProductEntities.size();
    }

    public void updateList(List<StockProductEntity> stockProducts, int listType){
        this.stockProductEntities = stockProducts;
        this.listType = listType;
        notifyDataSetChanged();
    }
}
