package com.goleep.driverapp.helpers.uihelpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.goleep.driverapp.interfaces.EditTextListener;


/**
 * Created by kunalsingh on 11/04/17.
 */

public class EditTextHelper {

    private EditTextListener editTextListener;

    public EditTextHelper(EditTextListener editTextListener) {
        this.editTextListener = editTextListener;
    }

    public void attachTextChangedListener(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextListener.afterTextChanged(editable);
            }
        });
    }
}
