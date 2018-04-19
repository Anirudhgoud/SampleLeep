package com.goleep.driverapp.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.DeliveryOrderItemEventListener;
import com.goleep.driverapp.interfaces.ProductCheckListener;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ProductEntity;
import com.goleep.driverapp.services.room.entities.StockProductEntity;
import com.goleep.driverapp.utils.AppUtils;

import java.util.Locale;

/**
 * Created by anurag on 28/02/18.
 */

public class OrderItemsViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private CustomTextView tvProductName, tvProductQuantity, tvAmount, tvUnits;
    private CheckBox productCheckbox;
    private TextView tvReturnReason;
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
        tvAmount.setText(context.getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(itemView.getContext()), itemTotalPriceText(value)));
        productCheckbox.setVisibility(View.GONE);
        tvUnits.setOnClickListener(v -> deliveryOrderItemEventListener.onUnitsTap(product.getId(), product.getQuantity()));
        ReturnReason returnReason = product.getReturnReason();
        if(returnReason != null && returnReason.getReason() != null){
            tvReturnReason.setText(returnReason.getReason());
            tvReturnReason.setVisibility(View.VISIBLE);
            tvUnits.setText(String.valueOf(product.getReturnQuantity()));
            tvAmount.setText(context.getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(itemView.getContext()), itemTotalPriceText(product.getTotalReturnsPrice())));
        } else {
            tvReturnReason.setVisibility(View.GONE);
            tvUnits.setText(String.valueOf(product.getQuantity()));
            tvAmount.setText(context.getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(itemView.getContext()), itemTotalPriceText(product.getTotalPrice())));
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
        tvAmount.setText(context.getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(itemView.getContext()), itemTotalPriceText(value)));

        productCheckbox.setChecked(orderItem.isSelected());
        productCheckbox.setOnClickListener(v -> deliveryOrderItemEventListener.onCheckboxTap(orderItem.getId(), productCheckbox.isChecked()));
        tvUnits.setOnClickListener(v -> deliveryOrderItemEventListener.onUnitsTap(orderItem.getId(), orderItem.getMaxQuantity()));
    }

    private String itemTotalPriceText(double value) {
        return String.format(Locale.getDefault(), "%.02f", value);
    }

    public void bindData(StockProductEntity stockProductEntity, int productType, int position) {
        tvProductName.setText(stockProductEntity.getProductName() == null ? "" :
                stockProductEntity.getProductName());
        tvProductQuantity.setText(context.getString(R.string.weight_with_units,
                stockProductEntity.getWeight(), stockProductEntity.getWeightUnit()));
        tvUnits.setText(String.valueOf(stockProductEntity.getQuantity(productType)));

        double value = stockProductEntity.getQuantity(productType) * stockProductEntity.getDefaultPrice();
        tvAmount.setText(context.getString(R.string.value_with_currency_symbol, AppUtils.userCurrencySymbol(itemView.getContext()), itemTotalPriceText(value)));
        productCheckbox.setChecked(stockProductEntity.isSelected());
        productCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryOrderItemEventListener.onCheckboxTap(
                        stockProductEntity.getId(), productCheckbox.isChecked());
                productCheckListener.onItemChecked(stockProductEntity, productCheckbox.isChecked(), position);
            }
        });
        tvUnits.setOnClickListener(v -> deliveryOrderItemEventListener.onUnitsTap(
                stockProductEntity.getId(), stockProductEntity.getMaxQuantity(productType)));
    }

    public void setProductCheckListener(ProductCheckListener productCheckListener) {
        this.productCheckListener = productCheckListener;
    }
}
