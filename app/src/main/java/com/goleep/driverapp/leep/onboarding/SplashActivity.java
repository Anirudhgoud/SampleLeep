package com.goleep.driverapp.leep.onboarding;


import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.goleep.driverapp.R;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.utils.LogUtils;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SplashActivity extends ParentAppCompatActivity implements OnCompleteListener<LocationSettingsResponse> {

    protected static final String TAG = "SplashActivity";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_splash);
    }

    @Override
    public void doInitialSetup() {
        final Handler handler = new Handler();
        handler.postDelayed(this::createLocationSettingsRequest, 500);
    }

    private void goToNextActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClickWithId(int resourceId) {
    }

    private void createLocationSettingsRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> locationSettingsTask =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        locationSettingsTask.addOnCompleteListener(this);
    }

    private void displayLocationResolvableDialog(ApiException exception) {
        try {
            ResolvableApiException resolvable = (ResolvableApiException) exception;
            resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
        try {
            task.getResult(ApiException.class);
            goToNextActivity();
        } catch (ApiException exception) {
            handleAPIException(exception);
        }
    }

    private void handleAPIException(ApiException exception) {
        switch (exception.getStatusCode()) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                displayLocationResolvableDialog(exception);
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                LogUtils.error(TAG, "Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.");
                goToNextActivity();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
            goToNextActivity();
        } else {
            finish();
        }
    }
}
