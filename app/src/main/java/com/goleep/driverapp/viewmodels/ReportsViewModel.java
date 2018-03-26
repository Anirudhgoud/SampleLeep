package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.stetho.common.LogUtil;
import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.RequestConstants;
import com.goleep.driverapp.constants.SharedPreferenceKeys;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.ReportAttr;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.ReportsDataParser;
import com.goleep.driverapp.services.storage.LocalStorageService;
import com.goleep.driverapp.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubham on 23/03/2018.
 */

public class ReportsViewModel extends AndroidViewModel {
    private final String DATE_START = "start_date";
    private final String DATE_END = "end_date";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public ReportsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getTodaysReports(final UILevelNetworkCallback reportCallBack) {
        makeNetworkRequest(reportCallBack, getTodaysDateStringParameter());
    }

    public void getThisWeekReports(final UILevelNetworkCallback reportCallBack) {
        makeNetworkRequest(reportCallBack, getDatesThisWeek());
    }

    public void getThisMonthReport(final UILevelNetworkCallback reportCallBack) {
        makeNetworkRequest(reportCallBack, getDatesThisMonth());
    }

    private void makeNetworkRequest(final UILevelNetworkCallback reportCallBack, String urlQureyGetParameterString) {
        LogUtils.debug("Reportslog", UrlConstants.REPORT_URL + urlQureyGetParameterString);
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.REPORT_URL + urlQureyGetParameterString,
                null, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        if (response != null) {
                            switch (type) {
                                case NetworkConstants.SUCCESS:
                                    JSONObject userObj = (JSONObject) response.opt(0);
                                    ReportAttr reportAttr = new ReportsDataParser().reportsDataByParsingJsonResponse(userObj);
                                    List<ReportAttr> listReportAttr = new ArrayList<>();
                                    listReportAttr.add(reportAttr);
                                    reportCallBack.onResponseReceived(listReportAttr, false, null, false);
                                    break;
                                case NetworkConstants.FAILURE:
                                    reportCallBack.onResponseReceived(null, false, errorMessage, false);
                                    break;
                                case NetworkConstants.NETWORK_ERROR:
                                    reportCallBack.onResponseReceived(null, true, errorMessage, false);
                                    break;
                                case NetworkConstants.UNAUTHORIZED:
                                    reportCallBack.onResponseReceived(null, false, errorMessage, true);
                            }
                        } else {
                            reportCallBack.onResponseReceived(null, false, errorMessage, false);
                        }
                    }
                });

    }

    private String getDatesThisWeek() {
        Calendar calendar = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date date1 = null, date2 = null;
        try {
            date1 = simpleDateFormat.parse(simpleDateFormat.format(c1.getTime()));
            date2 = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date1.after(date2)) {
            int i = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
            calendar.add(Calendar.DATE, -i - 6);
            Date start = calendar.getTime();
            calendar = Calendar.getInstance();
            return getUrlQueryParameterFormater(simpleDateFormat.format(start), simpleDateFormat.format(calendar.getTime()));

        } else {

            return getUrlQueryParameterFormater(simpleDateFormat.format(c1.getTime()), simpleDateFormat.format(calendar.getTime()));
        }

    }

    private String getDatesThisMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = simpleDateFormat.format(calendar.getTime());
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return getUrlQueryParameterFormater(simpleDateFormat.format(calendar.getTime()), endDate);
    }

    private String getTodaysDateStringParameter() {
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());
        return getUrlQueryParameterFormater(date, date);
    }

    private String getUrlQueryParameterFormater(String date1, String date2) {
        return "?" + DATE_START + "=" + date1 + "&" + DATE_END + "=" + date2;
    }
}
