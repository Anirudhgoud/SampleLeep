package com.tracotech.tracoman.helpers.uihelpers;

import android.text.Layout;
import android.text.TextPaint;

/**
 * Created by vishalm on 02/05/18.
 */
public class PrintableLine {
    private String text;
    private Layout.Alignment alignment;
    private TextPaint textPaint;

    public PrintableLine(String text, Layout.Alignment alignment, TextPaint tp) {
        this.text = text;
        this.alignment = alignment;
        this.textPaint = tp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Layout.Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Layout.Alignment alignment) {
        this.alignment = alignment;
    }

    public TextPaint getTextPaint() {
        return textPaint;
    }

    public void setTextPaint(TextPaint textPaint) {
        this.textPaint = textPaint;
    }
}
