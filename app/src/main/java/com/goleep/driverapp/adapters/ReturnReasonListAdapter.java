package com.goleep.driverapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.RecyclerViewRadioButtonListener;
import com.goleep.driverapp.viewholders.ReturnReasonViewHolder;

import java.util.List;

/**
 * Created by vishalm on 05/04/18.
 */

public class ReturnReasonListAdapter extends RecyclerView.Adapter<ReturnReasonViewHolder> {

    private List<ReturnReason> returnReasons;
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

    public ReturnReasonListAdapter(List<ReturnReason> returnReasons){
        this.returnReasons = returnReasons;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public ReturnReasonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReturnReasonViewHolder holder = new ReturnReasonViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.return_reason_item, parent, false));
        holder.setCheckedChangeListener(recyclerViewRadioButtonListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReturnReasonViewHolder holder, int position) {
        holder.bind(returnReasons.get(position), position);
    }

    @Override
    public int getItemCount() {
        return returnReasons.size();
    }

    public ReturnReason getSelectedReason(){
        if(selectedIndex != -1)
            return returnReasons.get(selectedIndex);
         return null;
    }

    public void updateList(List<ReturnReason> returnReasons) {
        this.returnReasons = returnReasons;
        notifyDataSetChanged();
    }
}
