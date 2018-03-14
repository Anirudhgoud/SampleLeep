package com.goleep.driverapp.helpers.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.interfaces.AddSignatureListener;
import com.goleep.driverapp.utils.AppUtils;

/**
 * Created by anurag on 14/03/18.
 */

public class SignatureDialogFragment extends DialogFragment implements View.OnClickListener {

    private PaintView pvSignature;
    private CustomButton btDone;
    private ImageView ivClose;
    private CustomTextView tvSignatureError;

    private AddSignatureListener addSignatureListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.signature_dialog_layout, null);
        connectUIElements(rootView);
        setCLickListeners();
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(rootView);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.addSignatureListener = (AddSignatureListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnCompleteListener");
        }
    }

    private void connectUIElements(View view) {
        pvSignature = view.findViewById(R.id.pv_signature);
        btDone = view.findViewById(R.id.bt_done);
        ivClose = view.findViewById(R.id.iv_close);
        tvSignatureError = view.findViewById(R.id.tv_signature_error);
    }

    private void setCLickListeners() {
        btDone.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_done:
                if (pvSignature.isSignatureAdded()) {
                    Bitmap signature = AppUtils.bitmapFromView(pvSignature, pvSignature.getWidth(), pvSignature.getHeight());
                    addSignatureListener.onSignatureAdded(signature);
                    getDialog().cancel();
                } else {
                    tvSignatureError.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.iv_close:
                getDialog().cancel();
                break;
        }
    }
}
