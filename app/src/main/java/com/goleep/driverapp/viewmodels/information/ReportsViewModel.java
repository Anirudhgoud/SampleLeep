package com.goleep.driverapp.viewmodels.information;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.ReportsType;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Report;
import com.goleep.driverapp.interfaces.NetworkAPICallback;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.ReportsDataParser;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by shubham on 23/03/2018.
 */

public class ReportsViewModel extends AndroidViewModel {
    private final String DATE_START = "start_date";
    private final String DATE_END = "end_date";

    public ReportsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getReports(int type, final UILevelNetworkCallback reportCallBack) {

        switch (type) {
            case ReportsType.TODAY:
                makeNetworkRequest(reportCallBack, getTodaysDateStringParameter());
                break;
            case ReportsType.THIS_WEEK:
                makeNetworkRequest(reportCallBack, getDatesThisWeek());
                break;
            case ReportsType.THIS_MONTH:
                makeNetworkRequest(reportCallBack, getDatesThisMonth());
                break;
        }
    }

    private void makeNetworkRequest(final UILevelNetworkCallback reportCallBack, String urlQureyGetParameterString) {
        LogUtils.debug("Reportslog", UrlConstants.REPORT_URL + urlQureyGetParameterString);
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), UrlConstants.REPORT_URL + urlQureyGetParameterString,
                null, true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type) {
                            case NetworkConstants.SUCCESS:
                                JSONObject userObj = (JSONObject) response.opt(0);
                                Report report = new ReportsDataParser().reportsDataByParsingJsonResponse(userObj);
                                List<Report> listReport = new ArrayList<>();
                                listReport.add(report);
                                reportCallBack.onResponseReceived(listReport, false, null, false);
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

                    }
                });

    }

    private String getDatesThisWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date mondayDate = calendar.getTime();
        calendar = Calendar.getInstance();
        Date todaysDate = calendar.getTime();
        if (mondayDate.after(todaysDate)) {
            int daysToSubtract = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
            calendar.add(Calendar.DATE, -daysToSubtract - 6);
            Date start = calendar.getTime();
            calendar = Calendar.getInstance();
            return getUrlQueryParameterFormater(DateTimeUtils.REQUEST_DATE_FORMAT.format(start), DateTimeUtils.REQUEST_DATE_FORMAT.format(calendar.getTime()));
        } else {
            return getUrlQueryParameterFormater(DateTimeUtils.REQUEST_DATE_FORMAT.format(mondayDate), DateTimeUtils.REQUEST_DATE_FORMAT.format(calendar.getTime()));
        }

    }

    private String getDatesThisMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return getUrlQueryParameterFormater(DateTimeUtils.REQUEST_DATE_FORMAT.format(calendar.getTime()), DateTimeUtils.REQUEST_DATE_FORMAT.format(Calendar.getInstance().getTime()));
    }

    private String getTodaysDateStringParameter() {
        String date = DateTimeUtils.REQUEST_DATE_FORMAT.format(Calendar.getInstance().getTime());
        return getUrlQueryParameterFormater(date, date);
    }

    private String getUrlQueryParameterFormater(String date1, String date2) {
        return "?" + DATE_START + "=" + date1 + "&" + DATE_END + "=" + date2;
    }
}
