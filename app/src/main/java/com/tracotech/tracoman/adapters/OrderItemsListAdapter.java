package com.tracotech.tracoman.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.helpers.uimodels.Product;
import com.tracotech.tracoman.interfaces.DeliveryOrderItemEventListener;
import com.tracotech.tracoman.interfaces.ProductCheckListener;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.services.room.entities.StockProductEntity;
import com.tracotech.tracoman.viewholders.OrderItemsViewHolder;

import java.util.List;

/**
 * Created by anurag on 28/02/18.
 */

public class OrderItemsListAdapter extends RecyclerView.Adapter<OrderItemsViewHolder> {

    private List<?> itemsList;
    private DeliveryOrderItemEventListener deliveryOrderItemEventListener;
    private int productsType = -1;

    public OrderItemsListAdapter(List<?> itemsList){
        this.itemsList = itemsList;
    }

    public OrderItemsListAdapter(List<?> itemsList, int productsType){
        this.itemsList = itemsList;
        this.productsType = productsType;
    }

    private ProductCheckListener productCheckListener = new ProductCheckListener() {
        @Override
        public void onItemChecked(StockProductEntity stockProductEntity, boolean checked, int position) {
            List<StockProductEntity> stocks = (List<StockProductEntity>) itemsList;
            stockProductEntity.setSelected(checked);
            stocks.set(position, stockProductEntity);
            itemsList = stocks;
            notifyDataSetChanged();
        }
    };

    public void setOrderItemClickEventListener(DeliveryOrderItemEventListener deliveryOrderItemEventListener){
        this.deliveryOrderItemEventListener = deliveryOrderItemEventListener;
    }

    @Override
    public OrderItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OrderItemsViewHolder viewHolder = new OrderItemsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.do_details_list_item, parent, false), deliveryOrderItemEventListener);
        viewHolder.setProductCheckListener(productCheckListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderItemsViewHolder holder, int position) {
        if(itemsList.get(0) instanceof OrderItemEntity) {
            OrderItemEntity orderItem = (OrderItemEntity) itemsList.get(position);
            holder.bindData(orderItem);
        }else if(itemsList.get(0) instanceof StockProductEntity && productsType != -1){
            StockProductEntity stockProductEntity = (StockProductEntity) itemsList.get(position);
            holder.bindData(stockProductEntity, productsType, position);
        }else if(itemsList.get(0) instanceof Product){
            Product product = (Product) itemsList.get(position);
            holder.bindData(product);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void updateList(List<?> itemsList){
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }
}