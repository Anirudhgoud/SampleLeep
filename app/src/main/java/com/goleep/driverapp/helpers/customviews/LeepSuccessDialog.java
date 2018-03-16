package com.goleep.driverapp.helpers.customviews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.interfaces.SuccessDialogEventListener;

/**
 * Created by anurag on 15/03/18.
 */

public class LeepSuccessDialog extends Dialog implements View.OnClickListener {

    private String message;
    private SuccessDialogEventListener listener;
    private CustomButton btPrint;

    public LeepSuccessDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
    }

    public void setSuccessDialogEventListener(SuccessDialogEventListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_dialog_layout);
        btPrint = findViewById(R.id.bt_print);
        ((CustomTextView) findViewById(R.id.message)).setText(message);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.ok_button).setOnClickListener(this);
        findViewById(R.id.close_layout).setOnClickListener(this);
        btPrint.setOnClickListener(this);
    }

    public void setPrintButtonVisibility(boolean visibility) {
        btPrint.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                if (listener != null) listener.onOkButtonTap();
                break;

            case R.id.close_layout:
                if (listener != null) listener.onCloseButtonTap();
                break;

            case R.id.bt_print:
                if (listener != null) listener.onPrintButtonTap();
                break;
        }
        dismiss();
    }
}
