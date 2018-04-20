package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Report;

import org.json.JSONObject;

/**
 * Created by shubham on 23/03/2018.
 */

public class ReportsDataParser {
    public Report reportsDataByParsingJsonResponse(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        Report report =new Report();
        report.setTotalSales(jsonObject.optInt("total_sales"));
        report.setCashCollected(jsonObject.optInt("cash_collected"));
        report.setUnits(jsonObject.optInt("units"));
        report.setReturns(jsonObject.optInt("returns"));
        report.setLocations(jsonObject.optInt("locations"));
        return report;
    }
}
