package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubham on 23/03/2018.
 */

public class ReportsViewModel extends AndroidViewModel{
    private Context context;
    private  String date_start="start_date";
    private  String date_end="end_date";
    public ReportsViewModel(@NonNull Application application) {
        super(application);
        context=application.getApplicationContext();
    }


    public void getTodysRepors(final UILevelNetworkCallback reportCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context, UrlConstants.REPORT_URL,
                null,true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                JSONObject userObj = (JSONObject) response.opt(0);
                                ReportAttr reportAttr=new ReportsDataParser().reportsDataByParsingJsonResponse(userObj);
                                List<ReportAttr> listReportAttr=new ArrayList<>();
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
                    }
                });

    }

    public void getThisWeekRepors(final UILevelNetworkCallback reportCallBack) {
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context, UrlConstants.REPORT_URL+getDatesThisWeek(),
                null,true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                JSONObject userObj = (JSONObject) response.opt(0);
                                ReportAttr reportAttr=new ReportsDataParser().reportsDataByParsingJsonResponse(userObj);
                                List<ReportAttr> listReportAttr=new ArrayList<>();
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
                    }
                });
    }

    public void getthisMonthRepors(final UILevelNetworkCallback reportCallBack) {

        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(context, UrlConstants.REPORT_URL+getDatesThisMonth(),
                null,true, new NetworkAPICallback() {
                    @Override
                    public void onNetworkResponse(int type, JSONArray response, String errorMessage) {
                        switch (type){
                            case NetworkConstants.SUCCESS:
                                JSONObject userObj = (JSONObject) response.opt(0);
                                ReportAttr reportAttr=new ReportsDataParser().reportsDataByParsingJsonResponse(userObj);
                                List<ReportAttr> listReportAttr=new ArrayList<>();
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
                    }
                });

    }
    private  String getDatesThisWeek()
    {   Calendar c1 = Calendar.getInstance();
        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, 1);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        //last day of week
        c1.set(Calendar.DAY_OF_WEEK, 7);
        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH)+1;
        int day7 = c1.get(Calendar.DAY_OF_MONTH);
        return "?"+date_start+"="+day1+"/"+month1+"/"+year1+"&"+date_end+"="+day7+"/"+month7+"/"+year7;
    }


    private String getDatesThisMonth()
    {
        Calendar c1 = Calendar.getInstance();
        //first day of week
        c1.set(Calendar.DAY_OF_MONTH, 1);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH)+1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        //last day of week
        c1.set(Calendar.DAY_OF_MONTH, 30);
        int year7 = c1.get(Calendar.YEAR);
        int month7 = c1.get(Calendar.MONTH)+1;
        int day7 = c1.get(Calendar.DAY_OF_MONTH);
        return "?"+date_start+"="+day1+"/"+month1+"/"+year1+"&"+date_end+"="+day7+"/"+month7+"/"+year7;

    }

}
