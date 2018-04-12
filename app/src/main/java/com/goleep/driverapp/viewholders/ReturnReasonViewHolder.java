package com.goleep.driverapp.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.ReturnReason;
import com.goleep.driverapp.interfaces.RecyclerViewRadioButtonListener;

/**
 * Created by vishalm on 05/04/18.
 */

public class ReturnReasonViewHolder extends RecyclerView.ViewHolder {
    private TextView reasonTextView;
    private ImageView radioButton;
    private RecyclerViewRadioButtonListener listener;
    private View.OnClickListener checkedChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            listener.onChecked(position);
        }
    };

    public void setCheckedChangeListener(RecyclerViewRadioButtonListener checkedChangeListener) {
        this.listener = checkedChangeListener;
    }

    public ReturnReasonViewHolder(View itemView) {
        super(itemView);
        reasonTextView = itemView.findViewById(R.id.tv_reason);
        radioButton = itemView.findViewById(R.id.rb_reason);
        radioButton.setOnClickListener(checkedChangeListener);
    }

    public void bind(ReturnReason returnReason, int position) {
        reasonTextView.setText(returnReason.getReason());
        radioButton.setTag(position);
        if(returnReason.isSelected())
            radioButton.setImageResource(R.drawable.ic_selected_radio_button);
        else
            radioButton.setImageResource(R.drawable.ic_unselected_radio_button);
    }
}
