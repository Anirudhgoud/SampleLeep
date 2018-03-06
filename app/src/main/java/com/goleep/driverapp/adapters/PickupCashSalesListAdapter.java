package com.goleep.driverapp.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.interfaces.ItemCheckListener;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.Product;
import com.goleep.driverapp.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 22/02/18.
 */

public class PickupCashSalesListAdapter extends RecyclerView.Adapter<
        PickupCashSalesListAdapter.ViewHolder> {
    private List<DeliveryOrderItem> doDetailsList;
    private int selectedCount = 0;
    private ItemCheckListener itemCheckListener;
    public PickupCashSalesListAdapter(List<DeliveryOrderItem> doDetailsList){
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
            holder.bind(doDetailsList.get(position));
    }

    @Override
    public int getItemCount() {
        return doDetailsList.size();
    }

    public void updateList(List<DeliveryOrderItem> deliveryOrderItems) {
        doDetailsList.clear();
        doDetailsList.addAll(deliveryOrderItems);
        notifyDataSetChanged();
    }

    public void setItemCheckListener(ItemCheckListener itemCheckListener) {
        this.itemCheckListener = itemCheckListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView productNameTv, productQuantityTv, amountTv, unitsTv;
        private CheckBox productCheckbox;
        public ViewHolder(View itemView) {
            super(itemView);
            productNameTv = itemView.findViewById(R.id.product_name_text_view);
            productQuantityTv = itemView.findViewById(R.id.quantity_text_view);
            amountTv = itemView.findViewById(R.id.amount_text_view);
            unitsTv = itemView.findViewById(R.id.units_text_view);
            productCheckbox = itemView.findViewById(R.id.product_checkbox);
        }

        public void bind(final DeliveryOrderItem doDetails) {
            Product product = doDetails.getProduct();
            productNameTv.setText(product.getName());
            double value = doDetails.getQuantity() * doDetails.getPrice();
            productQuantityTv.setText(product.getWeight()+" "+product.getWeightUnit());
            unitsTv.setText(String.valueOf(doDetails.getQuantity()));
            amountTv.setText(AppUtils.userCurrencySymbol()+" "+String.valueOf(value));
            productCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked)
                        selectedCount++;
                    else selectedCount = selectedCount == 0 ? 0 : --selectedCount;
                    itemCheckListener.itemChecked(doDetails, isChecked);
                }
            });
        }
    }
}
