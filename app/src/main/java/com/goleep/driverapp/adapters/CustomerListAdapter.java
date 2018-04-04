package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.interfaces.CustomerClickEventListener;
import com.goleep.driverapp.viewholders.CustomerListViewHolder;

import java.util.List;

/**
 * Created by anurag on 29/03/18.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListViewHolder> {

    private List<Customer> customerList;
    private CustomerClickEventListener customerClickEventListener;

    public CustomerListAdapter(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public void setCustomerClickEventListener(CustomerClickEventListener customerClickEventListener) {
        this.customerClickEventListener = customerClickEventListener;
    }

    @Override
    public CustomerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomerListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_item_layout, parent, false), customerClickEventListener);
    }

    @Override
    public void onBindViewHolder(CustomerListViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.bindData(customer);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public void updateData(List<Customer> customerList){
        this.customerList.clear();
        this.customerList.addAll(customerList);
        notifyDataSetChanged();
    }
}
