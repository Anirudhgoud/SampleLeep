package com.tracotech.tracoman.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tracotech.tracoman.R;
import com.tracotech.tracoman.helpers.uimodels.Reason;

public class HeadersViewHolder extends RecyclerView.ViewHolder {

    TextView headerTextView;

    public HeadersViewHolder(View itemView) {
        super(itemView);
        headerTextView = itemView.findViewById(R.id.orders_header_text_view);
    }

    public void bind(Reason reason, int position) {
        String title = reason.getTitle();
        itemView.setTag(position);
        if (title == null) return;
        headerTextView.setText(title);
    }
}
