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

    public EditTextHelper(EditTextListener editTextListener){
        this.editTextListener = editTextListener;
    }

    public void attachTextChangedListener(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editTextListener != null){
                    editTextListener.onTextChanged(charSequence.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
