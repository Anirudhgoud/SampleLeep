package com.goleep.driverapp.interfaces;

import android.text.Editable;

/**
 * Created by kunalsingh on 11/04/17.
 */

public interface EditTextListener {
    default void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    default void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    default void afterTextChanged(Editable editable) {
    }
}
