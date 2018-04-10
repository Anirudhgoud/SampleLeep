package com.goleep.driverapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.goleep.driverapp.helpers.uimodels.Business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 05/04/2018.
 */

public class BusinessesListAdapter extends ArrayAdapter<Business> {
    private List<Business> listBusinessesData;
    private List<Business> mainListBusinessData;

    public BusinessesListAdapter(Context context, int res, int textViewResourceId, List<Business> listGetBusinesses) {
        super(context, res, textViewResourceId, listGetBusinesses);
        listBusinessesData = listGetBusinesses;
        mainListBusinessData = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @Nullable ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        Business business = getItem(position);
        if (business != null) {
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(business.getName());
        }
        return convertView;
    }

    public void updateData(List<Business> listdata) {
        listBusinessesData.clear();
        mainListBusinessData.clear();
        listBusinessesData.addAll(listdata);
        mainListBusinessData.addAll(listdata);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return getDataBusinessFilter;
    }

    private Filter getDataBusinessFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Business> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(mainListBusinessData);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Business data : mainListBusinessData) {
                    if (data.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(data);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                clear();
                List<Business> data = (List<Business>) results.values;
                addAll(data);
                notifyDataSetChanged();
            }else
                notifyDataSetInvalidated();

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Business) resultValue).getName();
        }
    };
}

