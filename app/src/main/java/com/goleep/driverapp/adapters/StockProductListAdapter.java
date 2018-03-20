package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.utils.AppUtils;

import java.util.List;

/**
 * Created by vishalm on 20/03/18.
 */

public class StockProductListAdapter extends RecyclerView.Adapter<StockProductListAdapter.ViewHolder> {

    private List<StockProductEntity> stockProductEntities;
    public static final int TYPE_DELIVERABLE = 0;
    public static final int TYPE_SELLABLE = 1;
    public static final int TYPE_RETURNED = 2;
    private int listType = TYPE_DELIVERABLE;

    public StockProductListAdapter(List<StockProductEntity> stockProducts){
        stockProductEntities = stockProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StockProductListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.do_details_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(stockProductEntities.size() > 0)
            holder.bind(stockProductEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return stockProductEntities.size();
    }

    public void setListType(int listType){
        this.listType = listType;
    }

    public void updateList(List<StockProductEntity> stockProducts, int listType){
        this.stockProductEntities = stockProducts;
        this.listType = listType;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CustomTextView productNameTv, productQuantityTv, amountTv, unitsTv;
        private CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            productNameTv = itemView.findViewById(R.id.product_name_text_view);
            productQuantityTv = itemView.findViewById(R.id.quantity_text_view);
            amountTv = itemView.findViewById(R.id.amount_text_view);
            unitsTv = itemView.findViewById(R.id.units_text_view);
            checkBox = itemView.findViewById(R.id.product_checkbox);
            checkBox.setVisibility(View.GONE);
        }

        public void bind(StockProductEntity stockProductEntity){
            productNameTv.setText(stockProductEntity.getProductName());
            double value = 0.0;
            switch (listType){
                case TYPE_DELIVERABLE :
                    value = stockProductEntity.getDeliverableQuantity() * stockProductEntity.getDefaultPrice();
                    unitsTv.setText(String.valueOf(stockProductEntity.getDeliverableQuantity()));
                    break;
                case TYPE_SELLABLE :
                    value = stockProductEntity.getSellableQuantity() * stockProductEntity.getDefaultPrice();
                    unitsTv.setText(String.valueOf(stockProductEntity.getSellableQuantity()));
                    break;
                case TYPE_RETURNED :
                    value = stockProductEntity.getDeliverableQuantity() * stockProductEntity.getDefaultPrice();
                    unitsTv.setText(String.valueOf(stockProductEntity.getReturnableQuantity()));
                    break;
            }
            //productQuantityTv.setText(productEntity.getWeight()+" "+ productEntity.getWeightUnit());
            amountTv.setText(AppUtils.userCurrencySymbol()+" "+String.valueOf(value));
        }
    }
}
