package com.goleep.driverapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 29/03/18.
 */

public class CustomerSearchArrayAdapter extends ArrayAdapter<Customer> {

    private List<Customer> customerList;
    private Context mContext;

    public CustomerSearchArrayAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        customerList = objects;
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
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

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Customer customer = (Customer) resultValue;
            return customer.getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                FilterResults filterResults = new FilterResults();
                filterResults.values = customerList;
                filterResults.count = customerList.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                ArrayList<Customer> resultList = (ArrayList<Customer>) results.values;
                clear();
                for (Customer customer : resultList) {
                    add(customer);
                }
                notifyDataSetChanged();
            }
        }
    };
}
