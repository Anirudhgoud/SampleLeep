package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.ReportAttrribute;

import org.json.JSONObject;

/**
 * Created by shubham on 23/03/2018.
 */

public class ReportsDataParser {
    public ReportAttrribute reportsDataByParsingJsonResponse(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        ReportAttrribute reportAttrribute =new ReportAttrribute();
        reportAttrribute.setTotalSales(jsonObject.optInt("total_sales"));
        reportAttrribute.setCashCollected(jsonObject.optInt("cash_collected"));
        reportAttrribute.setUnits(jsonObject.optInt("units"));
        reportAttrribute.setReturns(jsonObject.optInt("returns"));
        reportAttrribute.setLocations(jsonObject.optInt("locations"));
        return reportAttrribute;
    }
}
