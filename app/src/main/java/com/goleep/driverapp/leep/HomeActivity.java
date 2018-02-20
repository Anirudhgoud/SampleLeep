package com.goleep.driverapp.leep;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
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
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uihelpers.NonSwipeableViewPager;
import com.goleep.driverapp.helpers.uimodels.Driver;
import com.goleep.driverapp.helpers.uimodels.InnerDashboardUiModel;
import com.goleep.driverapp.helpers.uimodels.Summary;
import com.goleep.driverapp.helpers.uimodels.UserMeta;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.viewmodels.HomeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
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
    NonSwipeableViewPager viewPager;
    @BindView(R.id.signout) CustomButton signOutButton;
    private HashMap<String, CustomTextView> summaryTextViewMap = new HashMap<>();
    private Summary summary = new Summary();

    View.OnClickListener dashboardItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setupInnerDashboard((String)view.getTag());
            viewPager.setCurrentItem(1);
            setToolbarLeftIcon(R.drawable.ic_home);
        }
    };

    View.OnClickListener innerDashboardItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch ((String)view.getTag()){
                case InnerDashboardUiModel.TAG_DELIVERY_ORDERS:
                    Intent doIntent = new Intent(HomeActivity.this, DeliveryOrdersActivity.class);
                    startActivity(doIntent);
                    break;
                case InnerDashboardUiModel.TAG_PICKUP:
                    Intent pickupIntent = new Intent(HomeActivity.this, PickupActivity.class);
                    startActivity(pickupIntent);
                    break;
            }
        }
    };

    private UILevelNetworkCallback logoutCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if(toLogout){
                performSignOut();
            } else if(errorMessage == null){
                performSignOut();
            } else {
                showNetworkRelatedDialogs(errorMessage);
            }
        }
    };

    private UILevelNetworkCallback driverProfileCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(final List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if(errorMessage == null){
                if(uiModels.size() > 0){
                    HomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayDriverProfile((Driver)uiModels.get(0));
                        }
                    });
                }
            } else if(isDialogToBeShown){
                showNetworkRelatedDialogs(errorMessage);
            }
        }
    };

    private UILevelNetworkCallback summaryCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(final List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            if(toLogout){
                performSignOut();
            } else if(errorMessage == null){
                if(uiModels.size() > 0){
                    summary = (Summary) uiModels.get(0);
                }
            } else if(isDialogToBeShown){
                showNetworkRelatedDialogs(errorMessage);
            }
        }
    };



    private void displayDriverProfile(Driver driver) {
        View view = findViewById(R.id.profile_layout);
        ((CustomTextView)view.findViewById(R.id.name_textView)).setText(driver.getFirstName()+" "+driver.getLastName());
        ((CustomTextView)view.findViewById(R.id.place_text_view)).setText(driver.getCity()+", "+driver.getCountryName());
        ((CustomTextView)view.findViewById(R.id.deliveries_value_textview)).setText(driver.getCompletedDeliveryOrdersCount()+"");
        ((CustomTextView)view.findViewById(R.id.payment_collected_values_textview)).setText(driver.getPaymentCollected()+"");
        ((CustomTextView)view.findViewById(R.id.locations_layout_value_textview)).setText(driver.getDeliveryLocationsCount()+"");
        ((CustomTextView)view.findViewById(R.id.contact_text_view)).setText(driver.getContactNumber());
        ((CustomTextView)view.findViewById(R.id.address_text_view)).setText(driver.getAddressLine1()+"\n"+driver.getAddressLine2());
        ((CustomTextView)view.findViewById(R.id.driver_licence_text_view)).setText(driver.getLicenceNumber());
        ((CustomTextView)view.findViewById(R.id.register_number_text_view)).setText(driver.getVehicleNumber());
        setToolbarRightText(driver.getFirstName()+" "+driver.getLastName());
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        initView();
        populateProfile();
        populateSummary();
    }

    private void populateSummary() {
        viewModel.getSummary(summaryCallback);
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

        switch (tag){
            case TAG_PICKUP: setUpPickupDashboard();
                break;
            case TAG_DROPOFF: setUpReturnsDashboard();
                break;
            case TAG_INFO: setupInformationDashboard();
                break;
        }
    }




    private void setupInformationDashboard() {
        List<InnerDashboardUiModel> innerDashboardUiModels = new ArrayList<>();
        innerDashboardUiModels.add(new InnerDashboardUiModel("Stocks", "",
                "Items", String.valueOf(summary.getInformationStocks()), R.drawable.ic_stocks,
                R.drawable.rounded_rect_orange, InnerDashboardUiModel.TAG_STOCKS));
        innerDashboardUiModels.add(new InnerDashboardUiModel("History", "",
                "Dos", String.valueOf(summary.getInformationOnHistory()), R.drawable.ic_history,
                R.drawable.rounded_rect_bahama_blue, InnerDashboardUiModel.TAG_HISTORY));
        innerDashboardUiModels.add(new InnerDashboardUiModel("Reports", "",
                "Invoices", String.valueOf(summary.getInformationOnReports()), R.drawable.ic_reports,
                R.drawable.rounded_rect_olive, InnerDashboardUiModel.TAG_REPORTS));
        populateInnerDashboard(innerDashboardUiModels);

    }

    private void setUpReturnsDashboard() {
        List<InnerDashboardUiModel> innerDashboardUiModels = new ArrayList<>();
        innerDashboardUiModels.add(new InnerDashboardUiModel("Delivery Orders", "",
                "Items", String.valueOf(summary.getDropOffDeliveryOrdersCount()), R.drawable.ic_delivery_orders,
                R.drawable.rounded_rect_green, InnerDashboardUiModel.TAG_DELIVERY_ORDERS));
        innerDashboardUiModels.add(new InnerDashboardUiModel("Cash Sales", "Sellable Items",
                "Today", String.valueOf(summary.getDropOffCashSales()), R.drawable.ic_cash_sales, R.drawable.rounded_rect_red,
                InnerDashboardUiModel.TAG_CASH_SALES));
        innerDashboardUiModels.add(new InnerDashboardUiModel("Drop Off", "To Warehouse",
                "Items", String.valueOf(summary.getDropOffToWarehouse()), R.drawable.ic_drop_off_warehouse,
                R.drawable.rounded_rect_purple, InnerDashboardUiModel.TAG_DROP_OFF));
        populateInnerDashboard(innerDashboardUiModels);
    }

    private void setUpPickupDashboard() {
        List<InnerDashboardUiModel> innerDashboardUiModels = new ArrayList<>();
        innerDashboardUiModels.add(new InnerDashboardUiModel("Pick Up", "From Warehouse",
                "Items", String.valueOf(summary.getPickUpFromWarehouse()), R.drawable.ic_pickup_warehouse,
                R.drawable.rounded_rect_blue, InnerDashboardUiModel.TAG_PICKUP));
        innerDashboardUiModels.add(new InnerDashboardUiModel("Returns", "From Customer",
                "Today", String.valueOf(summary.getReturnsFromCustomers()), R.drawable.ic_returns_customers,
                R.drawable.rounded_rect_red, InnerDashboardUiModel.TAG_RETURNS));
        populateInnerDashboard(innerDashboardUiModels);
    }

    private void populateInnerDashboard(List<InnerDashboardUiModel> innerDashboardUiModels) {
        ViewGroup innerDashBoardView = viewPager.findViewWithTag("inner");
        innerDashBoardView.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        for ( InnerDashboardUiModel uimodel: innerDashboardUiModels) {
            View view = inflater.inflate(R.layout.inner_dashboard_item, null, false);
            ((CustomTextView)view.findViewById(R.id.top_label_textview)).setText(uimodel.getTopText());
            ((CustomTextView)view.findViewById(R.id.main_text)).setText(uimodel.getMainText());
            ((CustomTextView)view.findViewById(R.id.sub_text)).setText(uimodel.getSubText());
            ((CustomTextView)view.findViewById(R.id.top_value_textview)).setText(String.valueOf(uimodel.getTopNumber()));
            ((ImageView)view.findViewById(R.id.icon)).setImageResource(uimodel.getIconResId());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.card_view_height));
            view.findViewById(R.id.top_value_textview).setBackgroundResource(uimodel.getTopColorId());
            params.setMargins(0, 0, 0, 70);
            view.setLayoutParams(params);
            view.setTag(uimodel.getTag());
            view.setOnClickListener(innerDashboardItemClickListener);
            innerDashBoardView.addView(view);
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
