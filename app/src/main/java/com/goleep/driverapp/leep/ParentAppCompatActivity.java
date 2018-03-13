package com.goleep.driverapp.leep;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uihelpers.AlertDialogHelper;
import com.goleep.driverapp.interfaces.NetworkChangeListener;
import com.goleep.driverapp.services.network.NetworkChecker;
import com.goleep.driverapp.services.storage.LocalStorageService;

import java.util.Vector;

/**
 * Created by vishalm on 07/02/18.
 */

public abstract class ParentAppCompatActivity extends AppCompatActivity implements View.OnClickListener{
    public abstract void doInitialSetup();
    public abstract void onActivityCreated(Bundle savedInstanceState);
    public abstract void onClickWithId(int resourceId);

    private NetworkChangeListener networkChangeListener;
    private AlertDialogHelper alertDialogHelper;
    private Dialog progressBarDialog;

    private BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(networkChangeListener != null
                    && new NetworkChecker().isNetworkAvailable(ParentAppCompatActivity.this)){
                closeAllNetworkDialogs();
                networkChangeListener.onNetworkConnected();
            }
        }
    };

    private void registerReceiverForNetworkChange(){
        registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void unregisterReceiverForNetworkChange(){
        if(connectivityChangeReceiver != null){
            try{
                unregisterReceiver(connectivityChangeReceiver);
            }catch(IllegalArgumentException e){}
        }
    }

    protected void startNetworkMonitoring(NetworkChangeListener networkChangeListener){
        this.networkChangeListener = networkChangeListener;
        registerReceiverForNetworkChange();
    }

    protected void showNetworkRelatedDialogs(String message){
        alertDialogHelper = new AlertDialogHelper();
        alertDialogHelper.showOkAlertDialog(this, getResources().getString(R.string.error), message);
    }

    protected void showSuccessDialog(String message){
        alertDialogHelper = new AlertDialogHelper();
        alertDialogHelper.showSuccessDialog(this, message);
    }

    protected void showProgressDialog(){
        progressBarDialog = new Dialog(ParentAppCompatActivity.this, R.style.ProgressBarTheme);
        progressBarDialog.setContentView(LayoutInflater.from(this).inflate(
                R.layout.progress_dialog_layout, null, false), new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        progressBarDialog.setCancelable(false);
        ProgressBar progressBar = progressBarDialog.findViewById(R.id.progress_bar);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.green),
                android.graphics.PorterDuff.Mode.SRC_IN);
        progressBarDialog.getWindow().setGravity(Gravity.CENTER);
        progressBarDialog.show();
    }

    protected void dismissProgressDialog(){
        try{
            progressBarDialog.hide();
        }catch (Exception e){

        }
    }

    private void closeAllNetworkDialogs() {
        if(alertDialogHelper != null){
            Vector<AlertDialog> dialogs = alertDialogHelper.getDialogs();
            for (final AlertDialog dialog : dialogs){
                if (dialog != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
                }
            }
            alertDialogHelper = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        onActivityCreated(savedInstanceState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dismissProgressDialog();
        unregisterReceiverForNetworkChange();
    }

    protected void setResources(int resourceIdentifier) {
        setContentView(resourceIdentifier);
        doInitialSetup();
    }

    protected void setToolbarLeftIcon(int resId){
        CustomButton leftToolbarButton = findViewById(R.id.left_toolbar_button);
        leftToolbarButton.setVisibility(View.VISIBLE);
        leftToolbarButton.setOnClickListener(this);
        leftToolbarButton.setBackgroundResource(resId);
    }

    protected void setTitleIconAndText(String title, int resId){
        findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
        ((CustomTextView)findViewById(R.id.activity_title)).setText(title);
        findViewById(R.id.title_icon).setBackgroundResource(resId);
    }

    protected void setToolbarRightText(String text){
        CustomTextView leftToolbarButton = findViewById(R.id.right_toolbar_text);
        leftToolbarButton.setText(text);
        leftToolbarButton.setVisibility(View.VISIBLE);
        leftToolbarButton.setOnClickListener(this);
    }

    protected void setToolBarColor(int colorId){
        RelativeLayout toolbarContainer = findViewById(R.id.toolbar_container);
        toolbarContainer.setBackgroundColor(colorId);

    }

    protected void logoutUser(){
        LocalStorageService.sharedInstance().getLocalFileStore().clearAllPreferences(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    protected void requestLocationsPermission(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
        }
    }

    protected boolean isLocationsPremissionGranted(){
        return (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onClick(View view) {
        onClickWithId(view.getId());
    }
}
