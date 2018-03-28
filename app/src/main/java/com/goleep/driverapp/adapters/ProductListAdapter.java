package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.ReturnOrderItem;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.viewholders.StocksListViewHolder;

import java.util.List;

/**
 * Created by vishalm on 20/03/18.
 */

public class ProductListAdapter extends RecyclerView.Adapter<StocksListViewHolder> {

    private List<?> products;
    public static final int TYPE_DELIVERABLE = 0;
    public static final int TYPE_SELLABLE = 1;
    public static final int TYPE_RETURNED = 2;
    private int listType = TYPE_DELIVERABLE;

    public ProductListAdapter(List<?> stockProducts){
        products = stockProducts;
    }

    @Override
    public StocksListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StocksListViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.do_details_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StocksListViewHolder holder, int position) {
        if(products.get(0) instanceof StockProductEntity)
            holder.bind((StockProductEntity)products.get(position), listType);
        else if(products.get(0) instanceof ReturnOrderItem)
            holder.bind((ReturnOrderItem)products.get(position));
        else if(products.get(0) instanceof OrderItemEntity)
            holder.bind((OrderItemEntity)products.get(position));

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateList(List<StockProductEntity> stockProducts, int listType){
        this.products = stockProducts;
        this.listType = listType;
        notifyDataSetChanged();
    }

    public void updateList(List<OrderItemEntity> stockProducts){
        this.products = stockProducts;
        notifyDataSetChanged();
    }

    public void updateReturnOrdersList(List<ReturnOrderItem> stockProducts){
        this.products = stockProducts;
        notifyDataSetChanged();
    }
}
