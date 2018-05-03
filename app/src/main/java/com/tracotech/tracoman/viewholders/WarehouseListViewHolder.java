package com.tracotech.tracoman.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.services.room.entities.WarehouseEntity;
import com.tracotech.tracoman.utils.StringUtils;

/**
 * Created by vishalm on 28/03/18.
 */

public class WarehouseListViewHolder extends RecyclerView.ViewHolder {
    private TextView customerNameTextView;
    private TextView addressTextView;
    private Button selectButton;
    private TextView doAssignedCountTextView;
    private View.OnClickListener warehouseSelectionListener;

    public void setWarehouseSelectionListener(View.OnClickListener warehouseSelectionListener) {
        this.warehouseSelectionListener = warehouseSelectionListener;
    }

    public WarehouseListViewHolder(View itemView) {
        super(itemView);
        customerNameTextView = itemView.findViewById(R.id.customer_name_tv);
        addressTextView = itemView.findViewById(R.id.address_tv);
        selectButton = itemView.findViewById(R.id.select_bt);
        doAssignedCountTextView = itemView.findViewById(R.id.tv_do_count);
    }

    public void bind(WarehouseEntity warehouseEntity, int position, boolean showDoCount) {
        customerNameTextView.setText(warehouseEntity.getWareHouseName());
        addressTextView.setText(StringUtils.getFullAddress(warehouseEntity.getAddressLine1(),
                warehouseEntity.getAddressLine2(), warehouseEntity.getCity(),
                warehouseEntity.getState(), warehouseEntity.getPincode()));
        itemView.setTag(position);
        if(showDoCount && warehouseEntity.getDoAssignedCount() < 1) {
            selectButton.setBackgroundResource(R.drawable.rounded_grey_button);
            doAssignedCountTextView.setVisibility(View.GONE);
        }
        else if(warehouseEntity.getDoAssignedCount() > 0){
            selectButton.setBackgroundResource(R.drawable.rounded_green_button);
            doAssignedCountTextView.setVisibility(View.VISIBLE);
            doAssignedCountTextView.setText(String.valueOf(warehouseEntity.getDoAssignedCount()));
        } else if(!showDoCount){
            selectButton.setBackgroundResource(R.drawable.rounded_green_button);
            doAssignedCountTextView.setVisibility(View.GONE);
        }
        if(warehouseSelectionListener != null){
            itemView.setOnClickListener(warehouseSelectionListener);
        }
    }
}
