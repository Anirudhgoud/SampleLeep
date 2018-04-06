package com.goleep.driverapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.interfaces.CustomerClickEventListener;
import com.goleep.driverapp.utils.DateTimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA;
import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_SERVER_DATE_FORMAT;

/**
 * Created by anurag on 28/03/18.
 */

public class CustomerListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_last_delivery)
    TextView tvLastDelivery;
    private CustomerClickEventListener customerClickEventListener;

    public CustomerListViewHolder(View itemView, CustomerClickEventListener customerClickEventListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.customerClickEventListener = customerClickEventListener;
    }

    public void bindData(Customer customer) {
        tvCustomerName.setText(customer.getName() == null ? "" : customer.getName());
        tvAddress.setText(customer.getArea() == null ? "" : customer.getArea());
        tvLastDelivery.setText(dateToDisplay(customer.getLastDeliveryDate()));
        setItemListeners(customer);
    }

    private void setItemListeners(Customer customer) {
        itemView.setOnClickListener(v -> customerClickEventListener.onCustomerSelectTap(customer));
    }

    private String dateToDisplay(String dateString) {
        return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString, ORDER_SERVER_DATE_FORMAT,
                ORDER_DISPLAY_DATE_FORMAT_COMMA);
    }
}
