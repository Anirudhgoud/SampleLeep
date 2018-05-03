package com.tracotech.tracoman.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.ReturnOrderEntity;
import com.tracotech.tracoman.utils.StringUtils;

/**
 * Created by vishalm on 20/03/18.
 */

public class HistoryListViewHolder extends RecyclerView.ViewHolder {

    private TextView customerNameTv, doNumberTv, customerAddressTv, doValueTv, doLabelTv, tvOrdeTypeDescription;
    private View.OnClickListener detailsClickListener;

    public HistoryListViewHolder(View itemView) {
        super(itemView);
        customerNameTv = itemView.findViewById(R.id.tv_customer_name);
        doNumberTv = itemView.findViewById(R.id.tv_do_number);
        customerAddressTv = itemView.findViewById(R.id.tv_store_address);
        doValueTv = itemView.findViewById(R.id.tv_amount);
        doLabelTv = itemView.findViewById(R.id.tv_do_number_label);
        tvOrdeTypeDescription = itemView.findViewById(R.id.tv_order_type_description);
    }

    public void setDetailsClickListener(View.OnClickListener detailsClickListener) {
        this.detailsClickListener = detailsClickListener;
    }

    public void bind(DeliveryOrderEntity deliveryOrderEntity) {
        customerNameTv.setText(deliveryOrderEntity.getCustomerName());
        doLabelTv.setText(itemView.getContext().getResources().getString(R.string.do_number));
        doNumberTv.setText(deliveryOrderEntity.getDoNumber());
        customerAddressTv.setText(StringUtils.getAddress(deliveryOrderEntity.getDestinationAddressLine1(),
                deliveryOrderEntity.getDestinationAddressLine2()));
        doValueTv.setText(StringUtils.amountToDisplay(deliveryOrderEntity.getTotalValue(), itemView.getContext()));
        itemView.setTag("type_do#"+deliveryOrderEntity.getId());
        itemView.setOnClickListener(detailsClickListener);
        tvOrdeTypeDescription.setVisibility(View.GONE);
    }

    public void bind(ReturnOrderEntity returnOrderEntity) {
        doNumberTv.setText(String.valueOf(returnOrderEntity.getRoNumber()));
        doLabelTv.setText(itemView.getContext().getResources().getString(R.string.ro_number));
        doValueTv.setText(StringUtils.amountToDisplay((float) returnOrderEntity.getTotalValue(), itemView.getContext()));
        itemView.setTag("type_ro#"+returnOrderEntity.getRoNumber());
        itemView.setOnClickListener(detailsClickListener);
        String type = returnOrderEntity.getType();
        Context context = itemView.getContext();
        if (context == null) return;
        tvOrdeTypeDescription.setVisibility(View.VISIBLE);
        String orderTypeDescription = "";
        String locationName = "";
        String address = "";
        switch (type){
            case "driver":
                locationName = returnOrderEntity.getDestinationLocationName();
                orderTypeDescription = context.getString(R.string.returned_to_warehouse);
                address = StringUtils.getAddress(returnOrderEntity.getDestinationAddressLine1(), returnOrderEntity.getDestinationAddressLine2());
                break;
            case "customer":
                locationName = returnOrderEntity.getSourceLocationName();
                orderTypeDescription = context.getString(R.string.picked_from_customer);
                address = StringUtils.getAddress(returnOrderEntity.getSourceAddressLine1(), returnOrderEntity.getSourceAddressLine2());
                break;
        }
        if (address.isEmpty() || address.trim().equals(",")) customerAddressTv.setVisibility(View.GONE);
        else {
            customerAddressTv.setVisibility(View.VISIBLE);
            customerAddressTv.setText(address);
        }
        tvOrdeTypeDescription.setText(orderTypeDescription);
        customerNameTv.setText(locationName);
    }
}