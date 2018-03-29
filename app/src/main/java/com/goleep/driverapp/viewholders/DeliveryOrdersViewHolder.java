package com.goleep.driverapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.Distance;
import com.goleep.driverapp.interfaces.DeliveryOrderClickEventListener;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.DateTimeUtils;

import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA;
import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_SERVER_DATE_FORMAT;
import static com.goleep.driverapp.utils.DateTimeUtils.TWELVE_HOUR_TIME_FORMAT;
import static com.goleep.driverapp.utils.DateTimeUtils.TWENTY_FOUR_HOUR_TIME_FORMAT;

/**
 * Created by anurag on 16/02/18.
 */

public class DeliveryOrdersViewHolder extends RecyclerView.ViewHolder {

    private final CustomTextView tvCustomerName;
    private final CustomTextView tvStoreAddress;
    private final CustomTextView tvDoNumber;
    private final CustomTextView tvDate;
    private final CustomTextView tvSchedule;
    private final CustomTextView tvDeliveryEstimatedTime;
    private final CustomTextView tvAmount;
    private final CustomTextView tvItemsCount;
    private final CustomButton btDeliver;
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

    public void bindData(DeliveryOrderEntity deliveryOrder){
        tvCustomerName.setText(deliveryOrder.getCustomerName() == null ? "" : deliveryOrder.getCustomerName());
        tvStoreAddress.setText(getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2()));
        tvDoNumber.setText(deliveryOrder.getDoNumber() ==  null ? "-" : deliveryOrder.getDoNumber());
        tvDate.setText(dateToDisplay(deliveryOrder.getPreferredDeliveryDate()));
        tvSchedule.setText(timeToDisplay(deliveryOrder.getPreferredDeliveryTime()));
        tvDeliveryEstimatedTime.setText(getEstimatedDeliveryTimeText(deliveryOrder.getDistanceFromCurrentLocation()));
        tvAmount.setText(amountToDisplay(deliveryOrder.getTotalValue()));
        tvItemsCount.setText(String.valueOf(deliveryOrder.getDeliveryOrderItemsCount()));
        setDeliverButtonClickEvent(deliveryOrder.getId());
    }

    private void setDeliverButtonClickEvent(final Integer orderId){
        btDeliver.setOnClickListener(v -> deliveryOrderClickEventListener.onDeliverClicked(orderId));
    }

    private String dateToDisplay(String dateString){
        return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString, ORDER_SERVER_DATE_FORMAT,
                ORDER_DISPLAY_DATE_FORMAT_COMMA);
    }

    private String timeToDisplay(String timeString){
        if (timeString != null){
            String[] times = timeString.split(" - ");
            if(times.length == 2){
                String startTime = DateTimeUtils.convertdDate(times[0].trim(), TWENTY_FOUR_HOUR_TIME_FORMAT, TWELVE_HOUR_TIME_FORMAT);
                String endTime = DateTimeUtils.convertdDate(times[1].trim(), TWENTY_FOUR_HOUR_TIME_FORMAT, TWELVE_HOUR_TIME_FORMAT);
                return startTime + " - " + endTime;
            }
        }
        return "-";
    }

    private String amountToDisplay(Float amountString){
        String currencySymbol = AppUtils.userCurrencySymbol();
        return amountString != null ? (currencySymbol + " " + Math.round(amountString)) : (currencySymbol + " 0");
    }

    private String getAddress(String line1, String line2){
        String addressLine1 = line1 == null ? "" : line1;
        String addressLine2 = line2 == null ? "" : line2;
        String separator = line1 == null ? "" : ", ";
        return addressLine1 + separator + addressLine2;
    }

    private String getEstimatedDeliveryTimeText(Distance distance) {
        return (distance == null) ? "-" : (distance.getDurationText() == null ? "-" : distance.getDurationText());
    }
}
