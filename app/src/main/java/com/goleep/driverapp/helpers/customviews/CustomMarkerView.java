package com.goleep.driverapp.helpers.customviews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomTextView;

/**
 * Created by anurag on 22/03/18.
 */

public class CustomMarkerView extends LinearLayout {

    private ImageView ivMarkerImage;
    private CustomTextView tvText;

    public CustomMarkerView(Context context, String text, boolean isSelected) {
        super(context);
        View.inflate(context, R.layout.map_marker_title_layout, this);
        ivMarkerImage = findViewById(R.id.iv_marker);
        tvText = findViewById(R.id.time_to_reach_tv);
        measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        tvText.setText(text);
        if (isSelected) {
            ivMarkerImage.setImageResource(R.drawable.ic_marker_selected);
            tvText.setBackgroundResource(R.drawable.rounded_rect_with_blue_border);
        } else {
            ivMarkerImage.setImageResource(R.drawable.ic_marker);
            tvText.setBackgroundResource(R.drawable.rounded_rect_with_green_border);
        }
    }

}