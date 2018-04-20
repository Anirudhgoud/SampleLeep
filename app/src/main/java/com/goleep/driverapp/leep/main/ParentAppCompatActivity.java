package com.goleep.driverapp.leep.main;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uihelpers.AlertDialogHelper;
import com.goleep.driverapp.interfaces.NetworkChangeListener;
import com.goleep.driverapp.interfaces.OnPermissionResult;
import com.goleep.driverapp.leep.onboarding.LoginActivity;
import com.goleep.driverapp.services.network.NetworkChecker;
import com.goleep.driverapp.services.storage.LocalStorageService;

import java.util.Vector;

/**
 * Created by vishalm on 07/02/18.
 */

public abstract class ParentAppCompatActivity extends AppCompatActivity implements View.OnClickListener{

    public abstract void onActivityCreated(Bundle savedInstanceState);

    public abstract void doInitialSetup();

    public abstract void onClickWithId(int resourceId);

    private NetworkChangeListener networkChangeListener;
    private AlertDialogHelper alertDialogHelper;
    private Dialog progressBarDialog;

    private OnPermissionResult permissionResult;

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        onActivityCreated(savedInstanceState);
    }

    protected void setResources(int resourceIdentifier) {
        setContentView(resourceIdentifier);
        doInitialSetup();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        dismissProgressDialog();
        unregisterReceiverForNetworkChange();
    }

    protected void setToolbarLeftIcon(int resId){
        Button leftToolbarButton = findViewById(R.id.left_toolbar_button);
        leftToolbarButton.setVisibility(View.VISIBLE);
        leftToolbarButton.setOnClickListener(this);
        Drawable leftButtonDrawable = getResources().getDrawable(resId);
        leftToolbarButton.setCompoundDrawablesWithIntrinsicBounds(leftButtonDrawable, null, null, null);
    }

    protected void setTitleIconAndText(String title, int resId){
        findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.activity_title)).setText(title);
        if(resId != -1) {
            findViewById(R.id.title_icon).setBackgroundResource(resId);
        }
    }

    protected void setToolbarRightText(String text){
        TextView leftToolbarButton = findViewById(R.id.right_toolbar_text);
        leftToolbarButton.setText(text);
        leftToolbarButton.setVisibility(View.VISIBLE);
        leftToolbarButton.setOnClickListener(this);
    }

    protected void setToolBarColor(int colorId){
        RelativeLayout toolbarContainer = findViewById(R.id.toolbar_container);
        toolbarContainer.setBackgroundColor(colorId);
    }

    public void logoutUser(){
        LocalStorageService.sharedInstance().getLocalFileStore().clearAllPreferences(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        onClickWithId(view.getId());
    }

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

    public void showNetworkRelatedDialogs(String message){
        alertDialogHelper = new AlertDialogHelper();
        alertDialogHelper.showOkAlertDialog(this, getResources().getString(R.string.error), message);
    }

    public void showProgressDialog(){
        if (progressBarDialog == null) {
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
    }


    public void dismissProgressDialog() {
        if (progressBarDialog != null) {
            progressBarDialog.dismiss();
            progressBarDialog = null;
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

    protected boolean isPermissionGranted(String[] permissions){
        for(String permission : permissions){
            if(ActivityCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    protected void requestPermission(String[] permissions, OnPermissionResult permissionResult){
        this.permissionResult = permissionResult;
        if(!isPermissionGranted(permissions)){
            ActivityCompat.requestPermissions(this,
                    permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionResult != null) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionResult.onPermissionGranted();
            } else {
                permissionResult.onPermissionDenied();
            }
        }
    }
}
