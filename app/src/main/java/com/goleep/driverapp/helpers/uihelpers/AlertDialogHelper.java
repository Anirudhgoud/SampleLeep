package com.goleep.driverapp.helpers.uihelpers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;


import com.goleep.driverapp.R;

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
                    builder.setTitle(title).setMessage(message).setPositiveButton("OK", null);
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

    public Vector<AlertDialog> getDialogs() {
        return dialogs;
    }
}