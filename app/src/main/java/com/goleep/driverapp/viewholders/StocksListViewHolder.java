package com.goleep.driverapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.ReturnOrderItem;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.utils.AppUtils;

/**
 * Created by vishalm on 20/03/18.
 */

public class StocksListViewHolder extends RecyclerView.ViewHolder{
    private CustomTextView productNameTv, productQuantityTv, amountTv, unitsTv, returnReasonTv;
    private CheckBox checkBox;
    public StocksListViewHolder(View itemView) {
        super(itemView);
        productNameTv = itemView.findViewById(R.id.product_name_text_view);
        productQuantityTv = itemView.findViewById(R.id.quantity_text_view);
        amountTv = itemView.findViewById(R.id.amount_text_view);
        unitsTv = itemView.findViewById(R.id.units_text_view);
        checkBox = itemView.findViewById(R.id.product_checkbox);
        checkBox.setVisibility(View.GONE);
        returnReasonTv = itemView.findViewById(R.id.return_reason_tv);
    }

    public void bind(StockProductEntity stockProductEntity, int listType){
        productNameTv.setText(stockProductEntity.getProductName());
        double value = stockProductEntity.getQuantity(listType) * stockProductEntity.getDefaultPrice();
        unitsTv.setText(String.valueOf(stockProductEntity.getDeliverableQuantity()));
        productQuantityTv.setText(stockProductEntity.getWeight()+" "+ stockProductEntity.getWeightUnit());
        amountTv.setText(AppUtils.userCurrencySymbol()+" "+String.valueOf(value));
    }

    public void bind(OrderItemEntity orderItem){
        productNameTv.setText(orderItem.getProduct().getName());
        double value = orderItem.getQuantity() * orderItem.getPrice();
        unitsTv.setText(String.valueOf(orderItem.getQuantity()));
        productQuantityTv.setText(orderItem.getProduct().getWeight()+" "+ orderItem.getProduct().getWeightUnit());
        amountTv.setText(AppUtils.userCurrencySymbol()+" "+String.valueOf(value));
    }

    public void bind(ReturnOrderItem orderItem){
        productNameTv.setText(orderItem.getProduct().getName());
        double value = orderItem.getQuantity() * orderItem.getPrice();
        unitsTv.setText(String.valueOf(orderItem.getQuantity()));
        productQuantityTv.setText(orderItem.getProduct().getWeight()+" "+ orderItem.getProduct().getWeightUnit());
        amountTv.setText(AppUtils.userCurrencySymbol()+" "+String.valueOf(value));
        if(returnReasonTv != null){
            returnReasonTv.setVisibility(View.VISIBLE);
            returnReasonTv.setText(orderItem.getReason());
        }
    }
}