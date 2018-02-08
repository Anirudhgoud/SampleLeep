package com.goleep.driverapp.leep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uihelpers.AlertDialogHelper;
import com.goleep.driverapp.interfaces.NetworkChangeListener;
import com.goleep.driverapp.services.network.NetworkChecker;

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

    public boolean showNetworkRelatedDialogs(boolean isDialogToBeShown, String message){
        if(isDialogToBeShown){
            alertDialogHelper = new AlertDialogHelper();
            alertDialogHelper.showOkAlertDialog(this, getResources().getString(R.string.error), message);
            return true;
        }else{
            return false;
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
        unregisterReceiverForNetworkChange();
    }

    protected void setResources(int resourceIdentifier) {
        setContentView(resourceIdentifier);
        doInitialSetup();
    }

    @Override
    public void onClick(View view) {
        onClickWithId(view.getId());
    }
}
