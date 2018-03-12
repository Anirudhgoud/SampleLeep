package com.goleep.driverapp.leep;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.goleep.driverapp.R;

public class SplashActivity extends ParentAppCompatActivity {
    private Context context;
    @Override
    public void doInitialSetup() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_splash);
        context = this;
        final Handler handler = new Handler();
        //showProgressDialog();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                performNextActivity();
            }
        }, 1000);
    }

    private void performNextActivity() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClickWithId(int resourceId) {

    }
}
