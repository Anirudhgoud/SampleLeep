package com.goleep.driverapp.leep.main;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.goleep.driverapp.services.system.DriverLocationUpdateService;
import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.IntentConstants;
import com.goleep.driverapp.helpers.uihelpers.NonSwipeableViewPager;
import com.goleep.driverapp.helpers.uimodels.InnerDashboardUiModel;
import com.goleep.driverapp.helpers.uimodels.Summary;
import com.goleep.driverapp.interfaces.OnPermissionResult;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.dropoff.cashsales.CashSalesActivity;
import com.goleep.driverapp.leep.dropoff.deliveryorders.DropOffDeliveryOrdersActivity;
import com.goleep.driverapp.leep.dropoff.dropoff.DropoffWarehouseActivity;
import com.goleep.driverapp.leep.pickup.pickup.PickupWarehouseActivity;
import com.goleep.driverapp.leep.pickup.returns.ReturnsCustomerSelectActivity;
import com.goleep.driverapp.leep.info.HistoryActivity;
import com.goleep.driverapp.leep.info.ReportsActivity;
import com.goleep.driverapp.leep.info.StocksActivity;
import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.main.HomeViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends ParentAppCompatActivity {

    @BindView(R.id.left_toolbar_button)
    Button profileButton;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.close_button)
    ImageView closeButton;
    @BindView(R.id.dashboard_viewpager)
    NonSwipeableViewPager viewPager;
    @BindView(R.id.signout)
    Button signOutButton;
    @BindView(R.id.edit_profile_imageview)
    ImageView profileImage;

    private HomeViewModel viewModel;

    private RelativeLayout relativeLayout_pickup_cardview;
    private RelativeLayout relativeLayout_drop_off_cardview;
    private RelativeLayout relativeLayout_information_cardview;

    final int START_GALLERY_REQUEST_CODE = 101;
    final int START_PICKUP_ACTIVITY_CODE = 102;

    private View.OnClickListener dashboardItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setupInnerDashboard((String) view.getTag());
            viewPager.setCurrentItem(1);
            setToolbarLeftIcon(R.drawable.ic_home);
        }
    };

    private View.OnClickListener innerDashboardItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Summary summary = viewModel.getSummary();
            switch ((String) view.getTag()) {
                case InnerDashboardUiModel.TAG_DELIVERY_ORDERS:
                    if (summary.getDropOffDeliveryOrdersCount() != 0) {
                        Intent doIntent = new Intent(HomeActivity.this, DropOffDeliveryOrdersActivity.class);
                        startActivity(doIntent);
                    }else {
                        Toast.makeText(HomeActivity.this, getString(R.string.no_delivery_orders), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case InnerDashboardUiModel.TAG_CASH_SALES:
                    Intent cashSalesIntent = new Intent(HomeActivity.this, CashSalesActivity.class);
                    startActivity(cashSalesIntent);
                    break;

                case InnerDashboardUiModel.TAG_PICKUP:
                    if (summary.getPickUpFromWarehouse() != 0) {
                        Intent pickupIntent = new Intent(HomeActivity.this, PickupWarehouseActivity.class);
                        startActivityForResult(pickupIntent, START_PICKUP_ACTIVITY_CODE);
                    }else {
                        Toast.makeText(HomeActivity.this, getString(R.string.no_pickup_orders), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case InnerDashboardUiModel.TAG_REPORTS:
                    Intent reportsIntent = new Intent(HomeActivity.this, ReportsActivity.class);
                    startActivity(reportsIntent);
                    break;
                case InnerDashboardUiModel.TAG_HISTORY:
                    Intent historyIntent = new Intent(HomeActivity.this, HistoryActivity.class);
                    startActivity(historyIntent);
                    break;
                case InnerDashboardUiModel.TAG_STOCKS:
                    Intent stocksIntent = new Intent(HomeActivity.this, StocksActivity.class);
                    startActivity(stocksIntent);
                    break;
                case InnerDashboardUiModel.TAG_DROP_OFF:
                    if (summary.getDropOffToWarehouse() != 0) {
                        Intent dropoffIntent = new Intent(HomeActivity.this, DropoffWarehouseActivity.class);
                        startActivity(dropoffIntent);
                    }else {
                        Toast.makeText(HomeActivity.this, getString(R.string.no_dropoff_orders), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case InnerDashboardUiModel.TAG_RETURNS:
                    Intent returnsIntent = new Intent(HomeActivity.this, ReturnsCustomerSelectActivity.class);
                    startActivity(returnsIntent);
                    break;
            }
        }
    };

    private UILevelNetworkCallback logoutCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> logoutUser();

    private UILevelNetworkCallback driverProfileCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        if (toLogout)
            logoutUser();
        else if (errorMessage == null && uiModels != null && uiModels.size() > 0) {
            if (!HomeActivity.this.isFinishing())
                HomeActivity.this.runOnUiThread(() -> displayDriverProfile((DriverEntity) uiModels.get(0)));
        } else if (isDialogToBeShown) {
            showNetworkRelatedDialogs(errorMessage);
        }
    };

    private UILevelNetworkCallback summaryCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(final List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            if (uiModels == null) {
                if (toLogout) {
                    logoutUser();
                } else if (isDialogToBeShown){
                    showNetworkRelatedDialogs(errorMessage);
                }
            } else if (uiModels.size() > 0) {
                runOnUiThread(() -> {
                    Summary summary = (Summary) uiModels.get(0);
                    viewModel.setSummary(summary);
                    populateUiCount(summary);
                });
            }
        }
    };

    private BroadcastReceiver taskSuccessBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewModel.getSummary(summaryCallback);
        }
    };


    private void displayDriverProfile(DriverEntity driverEntity) {
        String driverName = StringUtils.toString(driverEntity.getFirstName(), "")
                + " " + StringUtils.toString(driverEntity.getLastName(), "");
        View view = findViewById(R.id.profile_layout);
        ((TextView) view.findViewById(R.id.name_textView)).setText(driverName);
        ((TextView) view.findViewById(R.id.deliveries_value_textview)).setText(
                StringUtils.toString(String.valueOf(driverEntity.getCompletedDeliveryOrdersCount()), ""));
        ((TextView) view.findViewById(R.id.payment_collected_values_textview)).setText(
                StringUtils.toString(String.valueOf(driverEntity.getPaymentCollected()), ""));
        ((TextView) view.findViewById(R.id.locations_layout_value_textview)).setText(
                StringUtils.toString(String.valueOf(driverEntity.getDeliveryLocationsCount()), ""));
        ((TextView) view.findViewById(R.id.contact_text_view)).setText(driverEntity.getContactNumber());
        ((TextView) view.findViewById(R.id.driver_licence_text_view)).setText(driverEntity.getLicenceNumber());
        ((TextView) view.findViewById(R.id.register_number_text_view)).setText(driverEntity.getVehicleNumber());
        setToolbarRightText(driverName);
        view.findViewById(R.id.edit_profile_pic_layout).setOnClickListener(this);
        if (driverEntity.getImageUrl() != null) {
            Glide.with(this).load(driverEntity.getImageUrl()).apply(new RequestOptions().circleCrop().placeholder(R.drawable.profile_image_placeholder)).into(profileImage);
        }
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        initView();
        populateProfile();
        populateSummary();
        registerBroadcastReceiver();
        startDriverLocationUpdateService();
    }

    private void registerBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(taskSuccessBroadcast,
                new IntentFilter(IntentConstants.TASK_SUCCESSFUL));
    }

    @Override
    public void onNewIntent(Intent intent){
        if(intent != null && intent.getBooleanExtra(IntentConstants.TASK_SUCCESSFUL, false)){
            viewPager.setCurrentItem(0);
        }
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
        setToolbarRightText("");
        profileButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        viewModel.getStocks();
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
        switch (resourceId) {
            case R.id.left_toolbar_button:
                if (viewPager.getCurrentItem() == 0)
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
            case R.id.edit_profile_pic_layout:
                openGallery();
                break;
        }
    }

    private void openGallery() {
        if (isPermissionGranted(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            startGallery();
        } else {
            requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new OnPermissionResult() {
                @Override
                public void onPermissionGranted() {
                    startGallery();
                }

                @Override
                public void onPermissionDenied() {
                    Toast.makeText(HomeActivity.this, getString(R.string.storage_permission_denied),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void startGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.image_picker)), START_GALLERY_REQUEST_CODE);
    }

    private void setupInnerDashboard(String tag) {
        final String TAG_PICKUP = "0";
        final String TAG_DROPOFF = "1";
        final String TAG_INFO = "2";

        Summary summary = viewModel.getSummary();
        switch (tag) {
            case TAG_PICKUP:
                setUpPickupDashboard(summary);
                break;
            case TAG_DROPOFF:
                setUpReturnsDashboard(summary);
                break;
            case TAG_INFO:
                setupInformationDashboard(summary);
                break;
        }
    }

    private void setupInformationDashboard(Summary summary) {
        int stocksCount = summary.getInformationStocks();
        int historyCount = summary.getInformationOnHistory();
        int reportsCount = summary.getInformationOnReports();

        List<InnerDashboardUiModel> innerDashboardUiModels = new ArrayList<>();
        innerDashboardUiModels.add(new InnerDashboardUiModel("Stocks", "",
                "Items", String.valueOf(stocksCount == -1 ? 0 : stocksCount), R.drawable.ic_stocks,
                R.drawable.rounded_rect_orange, InnerDashboardUiModel.TAG_STOCKS));
        innerDashboardUiModels.add(new InnerDashboardUiModel("History", "",
                "Dos", String.valueOf(historyCount == -1 ? 0 : historyCount), R.drawable.ic_history,
                R.drawable.rounded_rect_bahama_blue, InnerDashboardUiModel.TAG_HISTORY));
        innerDashboardUiModels.add(new InnerDashboardUiModel("Reports", "",
                "Invoices", String.valueOf(reportsCount == -1 ? 0 : reportsCount), R.drawable.ic_reports,
                R.drawable.rounded_rect_olive, InnerDashboardUiModel.TAG_REPORTS));
        populateInnerDashboard(innerDashboardUiModels);
    }

    private void setUpReturnsDashboard(Summary summary) {
        int deliveryOrderCount = summary.getDropOffDeliveryOrdersCount();
        int cashSalesCount = summary.getDropOffCashSales();
        int dropoffCount = summary.getDropOffToWarehouse();

        List<InnerDashboardUiModel> innerDashboardUiModels = new ArrayList<>();
        innerDashboardUiModels.add(new InnerDashboardUiModel("Delivery Orders", "",
                "Items", String.valueOf(deliveryOrderCount == -1 ? 0 : deliveryOrderCount), R.drawable.ic_delivery_orders,
                R.drawable.rounded_rect_green, InnerDashboardUiModel.TAG_DELIVERY_ORDERS));
        innerDashboardUiModels.add(new InnerDashboardUiModel("Cash Sales", "Sellable Items",
                "Today", String.valueOf(cashSalesCount == -1 ? 0 : cashSalesCount), R.drawable.ic_cash_sales, R.drawable.rounded_rect_red,
                InnerDashboardUiModel.TAG_CASH_SALES));
        innerDashboardUiModels.add(new InnerDashboardUiModel("Drop Off", "To Warehouse",
                "Items", String.valueOf(dropoffCount == -1 ? 0 : dropoffCount), R.drawable.ic_drop_off_warehouse,
                R.drawable.rounded_rect_purple, InnerDashboardUiModel.TAG_DROP_OFF));
        populateInnerDashboard(innerDashboardUiModels);
    }

    private void setUpPickupDashboard(Summary summary) {
        int pickUpCount = summary.getPickUpFromWarehouse();
        int returnsCount = summary.getReturnsFromCustomers();

        List<InnerDashboardUiModel> innerDashboardUiModels = new ArrayList<>();
        innerDashboardUiModels.add(new InnerDashboardUiModel("Pick Up", "From Warehouse",
                "Items", String.valueOf(pickUpCount == -1 ? 0 : pickUpCount), R.drawable.ic_pickup_warehouse,
                R.drawable.rounded_rect_blue, InnerDashboardUiModel.TAG_PICKUP));
        innerDashboardUiModels.add(new InnerDashboardUiModel("Returns", "From Customer",
                "Today", String.valueOf(returnsCount == -1 ? 0 : returnsCount), R.drawable.ic_returns_customers,
                R.drawable.rounded_rect_red, InnerDashboardUiModel.TAG_RETURNS));
        populateInnerDashboard(innerDashboardUiModels);
    }

    private void populateInnerDashboard(List<InnerDashboardUiModel> innerDashboardUiModels) {
        ViewGroup innerDashBoardView = viewPager.findViewWithTag("inner");
        ViewGroup dashboardContainer = innerDashBoardView.findViewById(R.id.scrollview_container);
        dashboardContainer.removeAllViewsInLayout();
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        for (InnerDashboardUiModel uimodel : innerDashboardUiModels) {
            View view = inflater.inflate(R.layout.inner_dashboard_item, null, false);
            ((TextView) view.findViewById(R.id.top_label_textview)).setText(uimodel.getTopText());
            ((TextView) view.findViewById(R.id.main_text)).setText(uimodel.getMainText());
            ((TextView) view.findViewById(R.id.sub_text)).setText(uimodel.getSubText());
            ((TextView) view.findViewById(R.id.top_value_textview)).setText(String.valueOf(uimodel.getTopNumber()));
            ((ImageView) view.findViewById(R.id.icon)).setImageResource(uimodel.getIconResId());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.card_view_height));
            view.findViewById(R.id.top_value_textview).setBackgroundResource(uimodel.getTopColorId());
            params.setMargins(0, 0, 0, 50);
            view.setLayoutParams(params);
            view.setTag(uimodel.getTag());
            view.setOnClickListener(innerDashboardItemClickListener);
            dashboardContainer.addView(view);
        }
    }

    class DashboardPagerAdapter extends PagerAdapter {

        public Object instantiateItem(ViewGroup collection, int position) {

            LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
            ViewGroup layout = null;

            switch (position) {
                case 0:
                    layout = (ViewGroup) inflater.inflate(R.layout.dashboard_view, collection, false);
                    relativeLayout_pickup_cardview = layout.findViewById(R.id.pickup_cardview);
                    relativeLayout_drop_off_cardview = layout.findViewById(R.id.drop_off_cardview);
                    relativeLayout_information_cardview = layout.findViewById(R.id.information_cardview);
                    relativeLayout_pickup_cardview.setTag("0");
                    relativeLayout_drop_off_cardview.setTag("1");
                    relativeLayout_information_cardview.setTag("2");
                    relativeLayout_pickup_cardview.setOnClickListener(dashboardItemClickListener);
                    relativeLayout_drop_off_cardview.setOnClickListener(dashboardItemClickListener);
                    relativeLayout_information_cardview.setOnClickListener(dashboardItemClickListener);
                    findDashboardViewsAndSet(relativeLayout_pickup_cardview, R.string.pickup, R.drawable.pickup_icon_bg,
                            R.drawable.ic_pickup_dashboard, R.string.pick_up__below_text);

                    findDashboardViewsAndSet(relativeLayout_drop_off_cardview, R.string.dropoff, R.drawable.drop_off_icon_bg,
                            R.drawable.ic_drop_off_dashboard, R.string.drop_off_below_text);

                    findDashboardViewsAndSet(relativeLayout_information_cardview, R.string.information, R.drawable.info_icon_bg,
                            R.drawable.ic_info_dashboard, R.string.information_below_text);

                    break;
                case 1:
                    layout = (ViewGroup) inflater.inflate(R.layout.inner_dashboard_view, collection, false);
                    layout.setTag("inner");
                    break;
            }

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
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
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            finish();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == START_GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = intent.getData();
            File sourceFile = new File(getRealPathFromURI(uri));
            Glide.with(this).load(uri).apply(new RequestOptions().circleCrop().placeholder(R.drawable.profile_image_placeholder)).into(profileImage);
            viewModel.uploadProfileImage(sourceFile);
        } else if (requestCode == START_PICKUP_ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.getStocks();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void findDashboardViewsAndSet(RelativeLayout layout, int mainText, int drawableIconBg,
                                          int drawableDashboard, int belowText) {

        ((TextView) layout.findViewById(R.id.main_text)).setText(getResources().getText(mainText));
        (layout.findViewById(R.id.icon_layout)).setBackground(ContextCompat.getDrawable(HomeActivity.this, drawableIconBg));
        ((ImageView) layout.findViewById(R.id.icon)).setImageResource(drawableDashboard);
        ((TextView) layout.findViewById(R.id.sub_text)).setText(getResources().getText(belowText));

    }
    private  void setCountValues(RelativeLayout layout,String count,int background) {
        TextView tvCount = layout.findViewById(R.id.count_text);
        tvCount.setText(count);
        tvCount.setBackground(ContextCompat.getDrawable(HomeActivity.this, background));
    }

    private void populateUiCount(Summary summary) {
        int pickupCount = summary.getPickUpCount();
        int dropoffCount = summary.getDropoffCount();
        int informationCount = summary.getInformationCount();
        setCountValues(relativeLayout_pickup_cardview,StringUtils.formatToOneDecimal(pickupCount == -1 ? 0 : pickupCount),R.drawable.pickup_icon_bg);
        setCountValues(relativeLayout_drop_off_cardview,StringUtils.formatToOneDecimal(dropoffCount == -1 ? 0 : dropoffCount),R.drawable.drop_off_icon_bg);
        setCountValues(relativeLayout_information_cardview,StringUtils.formatToOneDecimal(informationCount == -1 ? 0 : informationCount),R.drawable.info_icon_bg);
    }


    private String addZeroToSingleCharacter(String str) {
        if (str.length() == 1)
            return "0" + str;
        else return str;
    }

    private void startDriverLocationUpdateService(){
        Intent intent = new Intent(this, DriverLocationUpdateService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(taskSuccessBroadcast);
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
