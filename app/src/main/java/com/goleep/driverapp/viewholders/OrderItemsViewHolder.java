package com.goleep.driverapp.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ProductEntity;
import com.goleep.driverapp.utils.AppUtils;

import java.text.DecimalFormat;

/**
 * Created by anurag on 28/02/18.
 */

public class OrderItemsViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private CustomTextView tvProductName, tvProductQuantity, tvAmount, tvUnits;
    private CheckBox productCheckbox;
    private DeliveryOrderItemEventListener deliveryOrderItemEventListener;

    public OrderItemsViewHolder(View itemView, DeliveryOrderItemEventListener deliveryOrderItemEventListener) {
        super(itemView);
        context = itemView.getContext();
        this.deliveryOrderItemEventListener = deliveryOrderItemEventListener;
        tvProductName = itemView.findViewById(R.id.product_name_text_view);
        tvProductQuantity = itemView.findViewById(R.id.quantity_text_view);
        tvAmount = itemView.findViewById(R.id.amount_text_view);
        tvUnits = itemView.findViewById(R.id.units_text_view);
        productCheckbox = itemView.findViewById(R.id.product_checkbox);
        productCheckbox.setChecked(true);
    }

    public void bindData(final OrderItemEntity orderItem){
        ProductEntity product = orderItem.getProduct();
        if(product != null){
            tvProductName.setText(product.getName() == null ? "" : product.getName());
        }
        tvProductQuantity.setText(context.getString(R.string.weight_with_units, product.getWeight(), product.getWeightUnit()));
        tvUnits.setText(String.valueOf(orderItem.getQuantity()));

        double value = orderItem.getQuantity() * orderItem.getPrice();
        tvAmount.setText(context.getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(), itemTotalPriceText(value)));

        if(productCheckbox != null){
            productCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    deliveryOrderItemEventListener.onCheckboxTap(orderItem.getId(), isChecked);
                }
            });
        }
    }

    private String itemTotalPriceText(double value){
        return String.format("%.02f", value);
//        DecimalFormat decimalFormat = new DecimalFormat();
//        decimalFormat.setMaximumFractionDigits(2);
//        return decimalFormat.format(value);
    }

}
