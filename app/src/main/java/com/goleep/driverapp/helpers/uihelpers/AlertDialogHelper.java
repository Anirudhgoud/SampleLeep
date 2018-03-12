package com.goleep.driverapp.helpers.uihelpers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;


import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.AppConstants;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;

import java.util.Vector;


public class AlertDialogHelper {
    private Vector<AlertDialog> dialogs = new Vector<>();

    public void showOkAlertDialog(final Activity activity, final String title, final String message) {
        if(activity != null && !activity.isFinishing()){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
                    builder.setMessage(message).setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialogs.add(dialog);
                    try{
                        dialog.show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    public void showSuccessDialog(final Activity activity, final String message){
        if(activity != null && !activity.isFinishing()){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
                    final AlertDialog dialog = dialogBuilder.setView(R.layout.success_dialog_layout).create();
                    dialog.setCancelable(false);
                    dialog.show();
                    dialog.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.setResult(AppConstants.ACTIVITY_SUCCESS_RESULT);
                            activity.finish();
                            dialog.dismiss();
                        }
                    });

                    dialog.findViewById(R.id.close_layout).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            activity.setResult(AppConstants.ACTIVITY_SUCCESS_RESULT);
                            activity.finish();
                            dialog.dismiss();
                        }
                    });
                    ((CustomTextView)dialog.findViewById(R.id.message)).setText(message);
                }
            });
        }
    }

    public Vector<AlertDialog> getDialogs() {
        return dialogs;
    }
}
