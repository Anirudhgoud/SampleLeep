package com.goleep.driverapp.helpers.customfont;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.goleep.driverapp.R;
import com.goleep.driverapp.leep.LeepApp;


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
            Typeface typeface = ((LeepApp)context.getApplicationContext()).getTypeface(fontName);
            if(typeface != null){
                setTypeface(typeface);
            }
        }
    }

    private KeyImeChange keyImeChangeListener;

    public void setKeyImeChangeListener(KeyImeChange listener) {
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        void onDoneButtonPress();
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyImeChangeListener != null) {
            keyImeChangeListener.onDoneButtonPress();
        }
        return false;
    }

}
