package com.goleep.driverapp.viewmodels.information;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.constants.NetworkConstants;
import com.goleep.driverapp.constants.UrlConstants;
import com.goleep.driverapp.helpers.uimodels.Report;
import com.goleep.driverapp.interfaces.UILevelNetworkCallback;
import com.goleep.driverapp.services.network.NetworkService;
import com.goleep.driverapp.services.network.jsonparsers.ReportsDataParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 23/03/2018.
 */

public class ReportsViewModel extends AndroidViewModel {
    public ReportsViewModel(@NonNull Application application) {
        super(application);
    }

    public void getReports(String urlQureyGetParameterString, final UILevelNetworkCallback reportCallBack) {
        String reportsUrl = UrlConstants.REPORT_URL + "?period=" + urlQureyGetParameterString;
        NetworkService.sharedInstance().getNetworkClient().makeGetRequest(getApplication(), reportsUrl,
                null, true, (type, response, errorMessage) -> {
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

                });

    }
}
