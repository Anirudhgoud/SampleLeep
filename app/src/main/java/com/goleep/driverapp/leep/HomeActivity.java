package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.helpers.customfont.CustomButton;
import com.goleep.driverapp.helpers.uimodels.UserMeta;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.viewmodels.HomeViewModel;

import java.util.List;

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
    @BindView(R.id.dashboard_viewpager)
    ViewPager viewPager;
    @BindView(R.id.signout) CustomButton signOutButton;

    View.OnClickListener dashboardItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setupInnerDashboard((String)view.getTag());
            viewPager.setCurrentItem(1);
            setToolbarLeftIcon(R.drawable.ic_home);
        }
    };

    private UILevelNetworkCallback logoutCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage) {
            if(errorMessage == null){
                performSignOut();
            }
        }
    };

    private UILevelNetworkCallback driverProfileCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage) {

        }
    };

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        initView();
        populateProfile();
    }

    private void populateProfile() {
        viewModel.getDriverProfile(driverProfileCallback);
    }

    private void initView() {
        initDrawer();
        setToolbarLeftIcon(R.drawable.ic_profile);
        setToolbarRightText("xxx");
        profileButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
    }

    private void initDrawer() {
        NavigationView navigationView = findViewById(R.id.navigation);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = metrics.widthPixels;
        navigationView.setLayoutParams(params);
        closeButton.setOnClickListener(this);
        DashboardPagerAdapter adapter = new DashboardPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setResources(R.layout.activity_home);

    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button:
                if(viewPager.getCurrentItem() == 0)
                    drawerLayout.openDrawer(GravityCompat.START);
                else {
                    setToolbarLeftIcon(R.drawable.ic_profile);
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.close_button:
                drawerLayout.closeDrawers();
                break;
            case R.id.signout:
                viewModel.signout(logoutCallback);
                break;
        }
    }

    private void performSignOut() {
        LocalStorageService.sharedInstance().getLocalFileStore().clearAllPreferences(HomeActivity.this);
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupInnerDashboard(String tag) {
        final String TAG_PICKUP = "0";
        final String TAG_DROPOFF = "1";
        final String TAG_INFO = "2";
        ViewGroup innerDashBoardView = viewPager.findViewWithTag("inner");
        innerDashBoardView.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        switch (tag){
            case TAG_PICKUP: View pickupView = inflater.inflate(R.layout.pickup_dashboard_view,
                    null, false);
                pickupView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                innerDashBoardView.addView(pickupView);
                break;
            case TAG_DROPOFF: innerDashBoardView.addView(inflater.inflate(R.layout.drop_off_dashboard_view,
                    null, false));
                break;
            case TAG_INFO: innerDashBoardView.addView(inflater.inflate(R.layout.information_dashboard_view,
                    null, false));
                break;
        }
    }


    class DashboardPagerAdapter extends PagerAdapter {

        public Object instantiateItem(ViewGroup collection, int position) {

            LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
            ViewGroup layout = null;

            switch (position){
                case 0: layout = (ViewGroup) inflater.inflate(R.layout.dashboard_view, collection, false);
                    layout.findViewById(R.id.pickup_cardview).setOnClickListener(dashboardItemClickListener);
                    layout.findViewById(R.id.drop_off_cardview).setOnClickListener(dashboardItemClickListener);
                    layout.findViewById(R.id.information_cardview).setOnClickListener(dashboardItemClickListener);
                break;

                case 1: layout = (ViewGroup) inflater.inflate(R.layout.inner_dashboard_view, collection, false);
                layout.setTag("inner");
                break;
            }

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() != 0){
            viewPager.setCurrentItem(0);
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            finish();
        }
    }
}
