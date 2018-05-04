package com.tracotech.tracoman.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.helpers.uimodels.Product;
import com.tracotech.tracoman.helpers.uimodels.ReturnReason;
import com.tracotech.tracoman.interfaces.DeliveryOrderItemEventListener;
import com.tracotech.tracoman.interfaces.ProductCheckListener;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;
import com.tracotech.tracoman.services.room.entities.ProductEntity;
import com.tracotech.tracoman.services.room.entities.StockProductEntity;
import com.tracotech.tracoman.utils.AppUtils;
import com.tracotech.tracoman.utils.StringUtils;

import java.util.Locale;

/**
 * Created by anurag on 28/02/18.
 */

public class OrderItemsViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private TextView tvProductName, tvProductQuantity, tvAmount, tvUnits, tvReturnReason;
    private CheckBox productCheckbox;
    private DeliveryOrderItemEventListener deliveryOrderItemEventListener;
    private ProductCheckListener productCheckListener;

    public OrderItemsViewHolder(View itemView, DeliveryOrderItemEventListener deliveryOrderItemEventListener) {
        super(itemView);
        context = itemView.getContext();
        this.deliveryOrderItemEventListener = deliveryOrderItemEventListener;
        tvProductName = itemView.findViewById(R.id.product_name_text_view);
        tvProductQuantity = itemView.findViewById(R.id.quantity_text_view);
        tvAmount = itemView.findViewById(R.id.amount_text_view);
        tvUnits = itemView.findViewById(R.id.units_text_view);
        productCheckbox = itemView.findViewById(R.id.product_checkbox);
        tvReturnReason = itemView.findViewById(R.id.return_reason_tv);
        tvUnits.setBackground(context.getResources().getDrawable(R.drawable.rounded_border_green));
    }

    public void bindData(Product product) {
        tvProductName.setText(product.getProductName() == null ? "" :
                product.getProductName());
        tvProductQuantity.setText(context.getString(R.string.weight_with_units,
                product.getWeight(), product.getWeightUnit()));

        double value = product.getQuantity() * product.getPrice();
        tvAmount.setText(StringUtils.amountToDisplay((float) value, itemView.getContext()));
        productCheckbox.setVisibility(View.GONE);
        tvUnits.setOnClickListener(v -> deliveryOrderItemEventListener.onUnitsTap(product.getId(), product.getQuantity()));
        ReturnReason returnReason = product.getReturnReason();
        if(returnReason != null && returnReason.getReason() != null){
            tvReturnReason.setText(returnReason.getReason());
            tvReturnReason.setVisibility(View.VISIBLE);
            tvUnits.setText(String.valueOf(product.getReturnQuantity()));
            tvAmount.setText(StringUtils.amountToDisplay((float) product.getTotalReturnsPrice(), itemView.getContext()));
        } else {
            tvReturnReason.setVisibility(View.GONE);
            tvUnits.setText(String.valueOf(product.getQuantity()));
            tvAmount.setText(StringUtils.amountToDisplay((float) product.getTotalPrice(), itemView.getContext()));
        }
    }

    public void bindData(final OrderItemEntity orderItem) {
        ProductEntity product = orderItem.getProduct();
        if (product != null) {
            tvProductName.setText(product.getName() == null ? "" : product.getName());
        }
        tvProductQuantity.setText(context.getString(R.string.weight_with_units, product.getWeight(), product.getWeightUnit()));
        tvUnits.setText(String.valueOf(orderItem.getQuantity()));

        double value = orderItem.getQuantity() * orderItem.getPrice();
        tvAmount.setText(StringUtils.amountToDisplay((float) value, itemView.getContext()));

        productCheckbox.setChecked(orderItem.isSelected());
        productCheckbox.setOnClickListener(v -> deliveryOrderItemEventListener.onCheckboxTap(orderItem.getId(), productCheckbox.isChecked()));
        tvUnits.setOnClickListener(v -> deliveryOrderItemEventListener.onUnitsTap(orderItem.getId(), orderItem.getMaxQuantity()));
    }

    public void bindData(StockProductEntity stockProductEntity, int productType, int position) {
        tvProductName.setText(stockProductEntity.getProductName() == null ? "" :
                stockProductEntity.getProductName());
        tvProductQuantity.setText(context.getString(R.string.weight_with_units,
                stockProductEntity.getWeight(), stockProductEntity.getWeightUnit()));
        tvUnits.setText(String.valueOf(stockProductEntity.getQuantity(productType)));

        double value = stockProductEntity.getQuantity(productType) * stockProductEntity.getDefaultPrice();
        tvAmount.setText(StringUtils.amountToDisplay((float) value, itemView.getContext()));
        productCheckbox.setChecked(stockProductEntity.isSelected());
        productCheckbox.setOnClickListener(view -> {
            deliveryOrderItemEventListener.onCheckboxTap(
                    stockProductEntity.getId(), productCheckbox.isChecked());
            productCheckListener.onItemChecked(stockProductEntity, productCheckbox.isChecked(), position);
        });
        tvUnits.setOnClickListener(v -> deliveryOrderItemEventListener.onUnitsTap(
                stockProductEntity.getId(), stockProductEntity.getMaxQuantity(productType)));
    }

    public void setProductCheckListener(ProductCheckListener productCheckListener) {
        this.productCheckListener = productCheckListener;
    }
}
