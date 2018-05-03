package com.tracotech.tracoman.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.helpers.uimodels.Customer;

import java.util.List;

/**
 * Created by anurag on 29/03/18.
 */

public class CustomerSearchArrayAdapter extends ArrayAdapter<Customer> {

    private List<Customer> customerList;

    public CustomerSearchArrayAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        customerList = objects;
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Customer getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.customer_item_layout, parent, false);
        }
        Customer customer = customerList.get(position);
        TextView tvCustomerName = convertView.findViewById(R.id.tv_customer_name);
        tvCustomerName.setText(customer.getName() == null ? "" : customer.getName());
        TextView tvArea = convertView.findViewById(R.id.tv_address);
        tvArea.setText(customer.getArea() == null ? "" : customer.getArea());
        convertView.findViewById(R.id.ll_last_delivery_view).setVisibility(View.GONE);
        convertView.findViewById(R.id.bt_select).setVisibility(View.GONE);
        return convertView;
    }

    public void updateData(List<Customer> customerList) {
        this.customerList.clear();
        this.customerList.addAll(customerList);
        notifyDataSetChanged();
    }
}
