package com.goleep.driverapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.Distance;
import com.goleep.driverapp.interfaces.DeliveryOrderClickEventListener;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.StringUtils;

import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA;
import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_SERVER_DATE_FORMAT;

/**
 * Created by anurag on 16/02/18.
 */

public class DeliveryOrdersViewHolder extends RecyclerView.ViewHolder {

    private final TextView tvCustomerName;
    private final TextView tvStoreAddress;
    private final TextView tvDoNumber;
    private final TextView tvDate;
    private final TextView tvSchedule;
    private final TextView tvDeliveryEstimatedTime;
    private final TextView tvAmount;
    private final TextView tvItemsCount;
    private final Button btDeliver;
    private DeliveryOrderClickEventListener deliveryOrderClickEventListener;

    public DeliveryOrdersViewHolder(View itemView, DeliveryOrderClickEventListener deliveryOrderClickEventListener) {
        super(itemView);
        tvCustomerName = itemView.findViewById(R.id.tv_customer_name);
        tvStoreAddress = itemView.findViewById(R.id.tv_store_address);
        tvDoNumber = itemView.findViewById(R.id.tv_do_number);
        tvDate = itemView.findViewById(R.id.tv_date);
        tvSchedule = itemView.findViewById(R.id.tv_schedule);
        tvDeliveryEstimatedTime = itemView.findViewById(R.id.tv_estimated_time);
        tvAmount = itemView.findViewById(R.id.tv_amount);
        tvItemsCount = itemView.findViewById(R.id.tv_item_count);
        btDeliver = itemView.findViewById(R.id.bt_delivery);
        this.deliveryOrderClickEventListener = deliveryOrderClickEventListener;
    }

    public void bindData(DeliveryOrderEntity deliveryOrder) {
        tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
        tvStoreAddress.setText(StringUtils.getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2()));
        tvDoNumber.setText(deliveryOrder.getDoNumber() == null ? "-" : deliveryOrder.getDoNumber());
        tvDate.setText(DateTimeUtils.convertdDate(deliveryOrder.getPreferredDeliveryDate(), ORDER_SERVER_DATE_FORMAT, ORDER_DISPLAY_DATE_FORMAT_COMMA));
        tvSchedule.setText(DateTimeUtils.timeDurationIn12HrFormat(deliveryOrder.getPreferredDeliveryTime()));
        tvDeliveryEstimatedTime.setText(getEstimatedDeliveryTimeText(deliveryOrder.getDistanceFromCurrentLocation()));
        tvAmount.setText(StringUtils.amountToDisplay(deliveryOrder.getTotalValue(), itemView.getContext()));
        tvItemsCount.setText(String.valueOf(deliveryOrder.getDeliveryOrderItemsCount()));
        setDeliverButtonClickEvent(deliveryOrder.getId());
    }

    private void setDeliverButtonClickEvent(final Integer orderId) {
        itemView.setOnClickListener(v -> deliveryOrderClickEventListener.onDeliverClicked(orderId));
        btDeliver.setOnClickListener(v -> deliveryOrderClickEventListener.onDeliverClicked(orderId));
    }

    private String getEstimatedDeliveryTimeText(Distance distance) {
        return (distance == null) ? "-" : (distance.getDurationText() == null ? "-" : distance.getDurationText());
    }
}
