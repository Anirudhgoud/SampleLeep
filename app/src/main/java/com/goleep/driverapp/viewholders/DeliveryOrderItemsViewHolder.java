package com.goleep.driverapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;

/**
 * Created by anurag on 28/02/18.
 */

public class DeliveryOrderItemsViewHolder extends RecyclerView.ViewHolder {


    private CustomTextView tvProductName, tvProductQuantity, tvAmount, tvUnits;
    private CheckBox productCheckbox;
    private DeliveryOrderItemEventListener deliveryOrderItemEventListener;

    public DeliveryOrderItemsViewHolder(View itemView, DeliveryOrderItemEventListener deliveryOrderItemEventListener) {
        super(itemView);
        this.deliveryOrderItemEventListener = deliveryOrderItemEventListener;
        tvProductName = itemView.findViewById(R.id.product_name_text_view);
        tvProductQuantity = itemView.findViewById(R.id.quantity_text_view);
        tvAmount = itemView.findViewById(R.id.amount_text_view);
        tvUnits = itemView.findViewById(R.id.units_text_view);
        productCheckbox = itemView.findViewById(R.id.product_checkbox);
    }

    public void bindData(OrderItemEntity deliveryOrder){

    }
}
