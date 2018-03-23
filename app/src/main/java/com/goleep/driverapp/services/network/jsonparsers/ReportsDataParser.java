package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.ReportAttr;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.ReportsEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 23/03/2018.
 */

public class ReportsDataParser {
    public ReportAttr reportsDataByParsingJsonResponse(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
         ReportAttr reportAttr =new ReportAttr();
        //Todo
        reportAttr.setTotal_sales(jsonObject.optInt("total_sales"));
        reportAttr.setCash_collected(jsonObject.optInt("cash_collected"));
        reportAttr.setUnits(jsonObject.optInt("units"));
        reportAttr.setReturns(jsonObject.optInt("returns"));
        reportAttr.setLocations(jsonObject.optInt("locations"));
        return  reportAttr;
    }
}
