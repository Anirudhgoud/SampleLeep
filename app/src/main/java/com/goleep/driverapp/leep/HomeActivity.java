package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.viewmodels.HomeViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends ParentAppCompatActivity {
    HomeViewModel viewModel;
    @BindView(R.id.left_toolbar_button)
    CustomButton profileButton;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.close_button)
    ImageView closeButton;

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        initView();
    }

    private void initView() {
        initDrawer();

        setToolbarLeftIcon(R.drawable.ic_profile);
        setToolbarRightText("xxx");
        profileButton.setOnClickListener(this);
    }

    private void initDrawer() {
        NavigationView navigationView = findViewById(R.id.navigation);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);
        closeButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_home);

    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.close_button:
                drawerLayout.closeDrawers();
        }
    }

}
