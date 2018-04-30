package com.goleep.driverapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.helpers.uimodels.Reason;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.RecyclerViewRadioButtonListener;
import com.goleep.driverapp.viewholders.HeadersViewHolder;
import com.goleep.driverapp.viewholders.ReturnReasonViewHolder;

import java.util.List;

/**
 * Created by vishalm on 05/04/18.
 */

public class ReturnReasonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Reason> returnReasons;
    private int selectedIndex = -1;

    public RecyclerViewRadioButtonListener recyclerViewRadioButtonListener = position -> {
        if(selectedIndex !=  -1) {
            returnReasons.get(selectedIndex).setSelected(false);
            notifyItemChanged(selectedIndex);
        }
        returnReasons.get(position).setSelected(true);
        notifyItemChanged(position);
        selectedIndex = position;
    };

    public ReturnReasonListAdapter(List<Reason> returnReasons){
        this.returnReasons = returnReasons;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    protected View inflate(int resourceID, ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(resourceID, viewGroup, false);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case AppConstants.TYPE_REASON:
                View reasonView = inflate(R.layout.return_reason_item, parent);
                ReturnReasonViewHolder holder = new ReturnReasonViewHolder(reasonView);
                holder.setCheckedChangeListener(recyclerViewRadioButtonListener);
                return holder;

            case AppConstants.TYPE_ORDERS_HEADER:
            default:
                View headerView = inflate(R.layout.orders_header_layout, parent);
                return new HeadersViewHolder(headerView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case AppConstants.TYPE_REASON:
                ReturnReason returnReason = (ReturnReason) returnReasons.get(position);
                ((ReturnReasonViewHolder) holder).bind(returnReason, position);
                break;
            case AppConstants.TYPE_ORDERS_HEADER:
                ((HeadersViewHolder) holder).bind(returnReasons.get(position), position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return returnReasons.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return returnReasons.size();
    }

    public Reason getSelectedReason(){
        if(selectedIndex != -1)
            return returnReasons.get(selectedIndex);
         return null;
    }

    public void updateList(List<Reason> returnReasons) {
        this.returnReasons = returnReasons;
        notifyDataSetChanged();
    }
}
