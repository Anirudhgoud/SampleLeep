package com.goleep.driverapp.leep.info;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.ReportsType;
import com.goleep.driverapp.helpers.uimodels.Report;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.leep.main.ParentAppCompatActivity;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.information.ReportsViewModel;

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
    TextView tvTotalSales;
    @BindView(R.id.activity_reports_tv_cash_collected)
    TextView tvCashCollected;
    @BindView(R.id.activity_reports_tv_returns)
    TextView tvReturns;
    @BindView(R.id.activity_reports_tv_units)
    TextView tvUnits;
    @BindView(R.id.activity_reports_tv_location)
    TextView tvLocation;
    private ReportsViewModel reportsViewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setResources(R.layout.activity_reports);
    }

    @Override
    public void doInitialSetup() {
        ButterKnife.bind(ReportsActivity.this);
        reportsViewModel = ViewModelProviders.of(ReportsActivity.this).get(ReportsViewModel.class);
        initView();
    }

    private void initView() {
        setTitleIconAndText(getString(R.string.reports), R.drawable.ic_reports_title);
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        rbToday.setOnClickListener(this);
        rbThisWeek.setOnClickListener(this);
        rbThisMonth.setOnClickListener(this);
        showProgressDialog();
        reportsViewModel.getReports(ReportsType.TODAY, reportsCallback);
    }

    private UILevelNetworkCallback reportsCallback = (uiModels, isDialogToBeShown, errorMessage, toLogout) -> {
        dismissProgressDialog();
        runOnUiThread(() -> handleReportsResponse(uiModels, isDialogToBeShown, errorMessage, toLogout));
    };

    private void handleReportsResponse(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
        dismissProgressDialog();
        if (uiModels == null) {
            if (toLogout) {
                logoutUser();
            } else if (isDialogToBeShown) {
                showNetworkRelatedDialogs(errorMessage);
            }
        } else if (uiModels.size() > 0) {
            runOnUiThread(() -> {
                Report report = (Report) uiModels.get(0);
                setReportData(report);
            });
        }
    }

    @Override
    public void onClickWithId(int resourceId) {
        switch (resourceId) {
            case R.id.left_toolbar_button:
                finish();
                break;
            case R.id.rb_today:
                initialiseReportCallback();
                reportsViewModel.getReports(ReportsType.TODAY, reportsCallback);
                break;
            case R.id.rb_this_week:
                initialiseReportCallback();
                reportsViewModel.getReports(ReportsType.THIS_WEEK, reportsCallback);
                break;
            case R.id.rb_this_month:
                initialiseReportCallback();
                reportsViewModel.getReports(ReportsType.THIS_MONTH, reportsCallback);
                break;
        }
    }

    private void initialiseReportCallback() {
        showProgressDialog();
        setReportData(null);
    }

    private void setReportData(Report report) {
        boolean isReportAvailable = report != null;
        tvCashCollected.setText(isReportAvailable ? StringUtils.amountToDisplay((float) report
                .getCashCollected(), this) : "");
        tvLocation.setText(isReportAvailable ? String.valueOf(report.getLocations()) : "");
        tvReturns.setText(isReportAvailable ? String.valueOf(report.getReturns()) + " " + getResources().getString(R.string.units) : "");
        tvTotalSales.setText(isReportAvailable ? StringUtils.amountToDisplay((float) report.
                getTotalSales(), this) : "");
        tvUnits.setText(isReportAvailable ? StringUtils.numberToDisplay(report.getUnits()) : "");
    }
}
