package com.goleep.driverapp.leep;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.ReportAttr;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.utils.AppUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.ReportsViewModel;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportsActivity extends ParentAppCompatActivity {

    @BindView(R.id.rb_today)
    RadioButton rbToday;
    @BindView(R.id.rb_this_week)
    RadioButton rbThisWeek;
    @BindView(R.id.rb_this_month)
    RadioButton rbThisMonth;
    @BindView(R.id.activity_reports_tv_total_sales)
    TextView tv_total_sales;
    @BindView(R.id.activity_reports_tv_cash_collected)
    TextView tv_cash_collected;
    @BindView(R.id.activity_reports_tv_returns)
    TextView tv_returns;
    @BindView(R.id.activity_reports_tv_units)
    TextView tv_units;
    @BindView(R.id.activity_reports_tv_location)
    TextView tv_location;
    private ReportsViewModel reportsViewModel;

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(ReportsActivity.this);
        reportsViewModel= ViewModelProviders.of(this).get(ReportsViewModel.class);
        initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_reports);

    }

    private void initView(){
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        rbToday.setOnClickListener(this);
        rbThisWeek.setOnClickListener(this);
        rbThisMonth.setOnClickListener(this);
        setTitleIconAndText(getString(R.string.reports), R.drawable.ic_reports_title);
        rbToday.setTypeface(AppUtils.getTypeface(ReportsActivity.this, "NotoSans-Regular"));
        rbThisWeek.setTypeface(AppUtils.getTypeface(ReportsActivity.this, "NotoSans-Regular"));
        rbThisMonth.setTypeface(AppUtils.getTypeface(ReportsActivity.this, "NotoSans-Regular"));
        reportsViewModel.getTodysRepors(reportsCallback);
        showProgressDialog();
    }
    private UILevelNetworkCallback reportsCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown, String errorMessage, boolean toLogout) {
            dismissProgressDialog();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    handleReportsResponse(uiModels, isDialogToBeShown, errorMessage);
                }
            });
        }
    };
    private void handleReportsResponse(List<?> uiModels, boolean isDialogToBeShown, String errorMessage) {
        if(errorMessage != null)
            showNetworkRelatedDialogs(errorMessage);
        else
        {
            dismissProgressDialog();
            ReportAttr reportAttr=(ReportAttr) uiModels.get(0);
            tv_cash_collected.setText(StringUtils.amountToDisplay((float)reportAttr.getCash_collected()));
            tv_location.setText(String.valueOf(reportAttr.getLocations()));
            tv_returns.setText(String.valueOf(reportAttr.getLocations()));
            tv_total_sales.setText(StringUtils.amountToDisplay((float)reportAttr.getTotal_sales()));
            tv_units.setText(String.valueOf(reportAttr.getUnits()));
        }


    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId){
            case R.id.left_toolbar_button : finish();
                break;
            case R.id.rb_today:
                reportsViewModel.getTodysRepors(reportsCallback);
                prePareUiRequest();
                break;
            case R.id.rb_this_week:
                reportsViewModel.getThisWeekRepors(reportsCallback);
                prePareUiRequest();
                break;
            case R.id.rb_this_month:
                reportsViewModel.getthisMonthRepors(reportsCallback);
                prePareUiRequest();
                break;
        }
    }
    private void prePareUiRequest()
    {
        tv_cash_collected.setText("");
        tv_location.setText("");
        tv_returns.setText("");
        tv_total_sales.setText("");
        tv_units.setText("");
        showProgressDialog();

    }
}
