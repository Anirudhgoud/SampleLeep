package com.tracotech.tracoman.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.constants.AppConstants;
import com.tracotech.tracoman.interfaces.ItemCheckListener;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.services.room.entities.ProductEntity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 22/02/18.
 */

public class PickupCashSalesListAdapter extends RecyclerView.Adapter<
        PickupCashSalesListAdapter.ViewHolder> {
    private List<OrderItemEntity> doDetailsList;
    private int selectedCount = 0;
    private ItemCheckListener itemCheckListener;
    public PickupCashSalesListAdapter(List<OrderItemEntity> doDetailsList){
        this.doDetailsList = doDetailsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.do_details_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(doDetailsList.size() > 0)
            holder.bind(doDetailsList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return doDetailsList.size();
    }

    public void updateList(List<OrderItemEntity> orderItemEntities) {
        doDetailsList.clear();
        doDetailsList.addAll(setItemType(orderItemEntities));
        notifyDataSetChanged();
    }

    private List<OrderItemEntity> setItemType(List<OrderItemEntity> orderItemEntities) {
        List<OrderItemEntity> orderItemEntityList = new ArrayList<>();
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItemEntity.setItemType(AppConstants.TYPE_CASH_SALES_ITEM);
            orderItemEntity.setSelected(false);
            orderItemEntityList.add(orderItemEntity);
        }
        return orderItemEntityList;
    }

    public void setItemCheckListener(ItemCheckListener itemCheckListener) {
        this.itemCheckListener = itemCheckListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTv, productQuantityTv, amountTv, unitsTv;
        private CheckBox productCheckbox;
        public ViewHolder(View itemView) {
            super(itemView);
            productNameTv = itemView.findViewById(R.id.product_name_text_view);
            productQuantityTv = itemView.findViewById(R.id.quantity_text_view);
            amountTv = itemView.findViewById(R.id.amount_text_view);
            unitsTv = itemView.findViewById(R.id.units_text_view);
            productCheckbox = itemView.findViewById(R.id.product_checkbox);
        }

        public void bind(final OrderItemEntity doDetails, int position) {
            ProductEntity productEntity = doDetails.getProduct();
            productNameTv.setText(productEntity.getName());
            double value = doDetails.getQuantity() * doDetails.getPrice();
            productQuantityTv.setText(productEntity.getWeight()+" "+ productEntity.getWeightUnit());
            unitsTv.setText(String.valueOf(doDetails.getQuantity()));
            amountTv.setText(StringUtils.amountToDisplay((float) value, itemView.getContext()));
            productCheckbox.setVisibility(View.VISIBLE);
            productCheckbox.setChecked(doDetails.isSelected());
            productCheckbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if(isChecked) selectedCount++;
                else selectedCount = selectedCount == 0 ? 0 : --selectedCount;
                doDetails.setSelected(isChecked);
                doDetailsList.set(position, doDetails);
                itemCheckListener.itemChecked(doDetails, isChecked);
            });
        }
    }
}
