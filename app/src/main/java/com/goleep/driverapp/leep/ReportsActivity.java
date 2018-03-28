package com.goleep.driverapp.leep;


import android.arch.lifecycle.ViewModelProviders;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RadioButton;

import com.goleep.driverapp.R;
import com.goleep.driverapp.constants.ReportsType;
import com.goleep.driverapp.helpers.customfont.CustomTextView;
import com.goleep.driverapp.helpers.uimodels.ReportAttrribute;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.utils.StringUtils;
import com.goleep.driverapp.viewmodels.ReportsViewModel;

import java.util.List;


import com.goleep.driverapp.helpers.uihelpers.FontProvider;

import com.goleep.driverapp.utils.FontUtils;

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
    CustomTextView tv_total_sales;
    @BindView(R.id.activity_reports_tv_cash_collected)
    CustomTextView tv_cash_collected;
    @BindView(R.id.activity_reports_tv_returns)
    CustomTextView tv_returns;
    @BindView(R.id.activity_reports_tv_units)
    CustomTextView tv_units;
    @BindView(R.id.activity_reports_tv_location)
    CustomTextView tv_location;
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
        setToolBarColor(getResources().getColor(R.color.light_green));
        setToolbarLeftIcon(R.drawable.ic_back_arrow);
        rbToday.setOnClickListener(this);
        rbThisWeek.setOnClickListener(this);
        rbThisMonth.setOnClickListener(this);
        setTitleIconAndText(getString(R.string.reports), R.drawable.ic_reports_title);

        Typeface typeface = FontProvider.getTypeface(FontProvider.REGULAR, this);
        rbToday.setTypeface(typeface);
        rbThisWeek.setTypeface(typeface);
        rbThisMonth.setTypeface(typeface);
        showProgressDialog();
        reportsViewModel.getReports(ReportsType.TODAY, reportsCallback);
    }

    private UILevelNetworkCallback reportsCallback = new UILevelNetworkCallback() {
        @Override
        public void onResponseReceived(List<?> uiModels, boolean isDialogToBeShown,
                                       String errorMessage, boolean toLogout) {
            dismissProgressDialog();
            runOnUiThread(() -> handleReportsResponse(uiModels, isDialogToBeShown, errorMessage, toLogout));
        }
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
                ReportAttrribute reportAttrribute = (ReportAttrribute) uiModels.get(0);
                setReportData(reportAttrribute);
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

    private void setReportData(ReportAttrribute reportAttrribute) {
        boolean isReportAvailable = reportAttrribute != null;
        tv_cash_collected.setText(isReportAvailable ? StringUtils.amountToDisplay((float) reportAttrribute
                .getCashCollected()) : "");
        tv_location.setText(isReportAvailable ? String.valueOf(reportAttrribute.getLocations()) : "");
        tv_returns.setText(isReportAvailable ? String.valueOf(reportAttrribute.getReturns()) + " " + getResources().getString(R.string.units) : "");
        tv_total_sales.setText(isReportAvailable ? StringUtils.amountToDisplay((float) reportAttrribute.
                getTotalSales()) : "");
        tv_units.setText(isReportAvailable ? StringUtils.numberToDisplay(reportAttrribute.getUnits()) : "");
    }
}
