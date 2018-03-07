package com.goleep.driverapp.helpers.customfont;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.goleep.driverapp.R;


public class CustomEditText extends AppCompatEditText {

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
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
            if(typeface != null){
                setTypeface(typeface);
            }
        }
    }

}
