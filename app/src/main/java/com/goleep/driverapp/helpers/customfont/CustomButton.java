package com.goleep.driverapp.helpers.customfont;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.goleep.driverapp.R;


public class CustomButton extends AppCompatButton {
    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(context, attrs);
    }


    private void parseAttributes(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs,
                R.styleable.TypefacedTextView);
        String fontName = styledAttrs
                .getString(R.styleable.TypefacedTextView_typeface);
        styledAttrs.recycle();
        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + fontName + ".ttf");
            setTypeface(typeface);
        }
    }

}
