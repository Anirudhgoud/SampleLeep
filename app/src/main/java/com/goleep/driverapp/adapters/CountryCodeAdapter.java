package com.goleep.driverapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.Country;

/**
 * Created by vishalm on 19/04/18.
 */
public class CountryCodeAdapter extends ArrayAdapter<Country> {

    public CountryCodeAdapter(Context context, int res){
        super(context, res);
    }

    @Override
    public View getView(int position, View convertView,
                        @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_country_code_item, parent, false);
        }
        Country country = getItem(position);
        if (country != null) {
            TextView textView = convertView.findViewById(R.id.text1);
            textView.setText(country.getDialCode());
        }
        convertView.setPadding(0, convertView.getPaddingTop(), 0, convertView.getPaddingBottom());
        return convertView;
    }
}
