package com.goleep.driverapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.Business;

/**
 * Created by shubham on 09/04/2018.
 */

public class BusinessCategoryAdapter extends ArrayAdapter<Business> {

    public BusinessCategoryAdapter(Context context, int res) {
        super(context, res);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView,
                        @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_spinner_layout, parent, false);
        }
        Business business = getItem(position);
        if (business != null) {
            TextView textView = convertView.findViewById(R.id.text1);
            textView.setText(business.getName());
        }
        return convertView;
    }
}
