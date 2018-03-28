package com.goleep.driverapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;
import com.goleep.driverapp.utils.StringUtils;

/**
 * Created by vishalm on 20/03/18.
 */

public class HistoryListViewHolder extends RecyclerView.ViewHolder {

    private CustomTextView customerNameTv, doNumberTv, customerAddressTv, doValueTv;
    private CustomButton detailsButton;
    private View.OnClickListener detailsClickListener;

    public HistoryListViewHolder(View itemView) {
        super(itemView);
        customerNameTv = itemView.findViewById(R.id.tv_customer_name);
        doNumberTv = itemView.findViewById(R.id.tv_do_number);
        customerAddressTv = itemView.findViewById(R.id.tv_store_address);
        doValueTv = itemView.findViewById(R.id.tv_amount);
        detailsButton = itemView.findViewById(R.id.details_button);

    }

    public void setDetailsClickListener(View.OnClickListener detailsClickListener) {
        this.detailsClickListener = detailsClickListener;
    }

    public void bind(DeliveryOrderEntity deliveryOrderEntity) {
        customerNameTv.setText(deliveryOrderEntity.getCustomerName());
        doNumberTv.setText(deliveryOrderEntity.getDoNumber());
        customerAddressTv.setText(StringUtils.getAddress(deliveryOrderEntity.getDestinationAddressLine1(),
                deliveryOrderEntity.getDestinationAddressLine2()));
        doValueTv.setText(StringUtils.amountToDisplay(deliveryOrderEntity.getTotalValue()));
        detailsButton.setTag("type_do#"+deliveryOrderEntity.getId());
        detailsButton.setOnClickListener(detailsClickListener);
    }

    public void bind(ReturnOrderEntity returnOrderEntity) {
        customerNameTv.setText(returnOrderEntity.getCustomerName());
        doNumberTv.setText(String.valueOf(returnOrderEntity.getRoNumber()));
        customerAddressTv.setText(StringUtils.getAddress(returnOrderEntity.getSourceAddressLine1(),
                returnOrderEntity.getSourceAddressLine2()));
        doValueTv.setText(StringUtils.amountToDisplay((float) returnOrderEntity.getTotalValue()));
        detailsButton.setTag("type_ro#"+returnOrderEntity.getRoNumber());
        detailsButton.setOnClickListener(detailsClickListener);
    }
}