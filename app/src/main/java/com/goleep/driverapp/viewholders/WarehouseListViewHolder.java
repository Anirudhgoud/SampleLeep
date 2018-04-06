package com.goleep.driverapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;
import com.goleep.driverapp.utils.StringUtils;

/**
 * Created by vishalm on 28/03/18.
 */

public class WarehouseListViewHolder extends RecyclerView.ViewHolder {
    private CustomTextView customerNameTextView;
    private CustomTextView addressTextView;
    private View.OnClickListener warehouseSelectionListener;

    public void setWarehouseSelectionListener(View.OnClickListener warehouseSelectionListener) {
        this.warehouseSelectionListener = warehouseSelectionListener;
    }

    public WarehouseListViewHolder(View itemView) {
        super(itemView);
        customerNameTextView = itemView.findViewById(R.id.customer_name_tv);
        addressTextView = itemView.findViewById(R.id.address_tv);
    }

    public void bind(WarehouseEntity warehouseEntity, int position) {
        customerNameTextView.setText(warehouseEntity.getWareHouseName());
        addressTextView.setText(StringUtils.getFullAddress(warehouseEntity.getAddressLine1(),
                warehouseEntity.getAddressLine2(), warehouseEntity.getCity(),
                warehouseEntity.getState(), warehouseEntity.getPincode()));
        itemView.setTag(position);
        if(warehouseSelectionListener != null){
            itemView.setOnClickListener(warehouseSelectionListener);
        }
    }
}
