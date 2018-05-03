package com.tracotech.tracoman.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.tracotech.tracoman.services.room.entities.StockProductEntity;

import java.util.List;

public class ProductSearchArrayAdapter extends ArrayAdapter<StockProductEntity> {

    private List<StockProductEntity> productList;

    public ProductSearchArrayAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        productList = objects;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public StockProductEntity getItem(int position) {
        return productList.get(position);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        StockProductEntity entity = productList.get(position);
        TextView tvProductName = convertView.findViewById(android.R.id.text1);
        tvProductName.setText(entity.getProductName() == null ? "" : entity.getProductName());
        return convertView;
    }

    public void updateData(List<StockProductEntity> productList) {
        this.productList.clear();
        this.productList.addAll(productList);
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
            StockProductEntity entity = (StockProductEntity) resultValue;
            return entity.getProductName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;
                filterResults.count = productList.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            }
        }
    };
}
