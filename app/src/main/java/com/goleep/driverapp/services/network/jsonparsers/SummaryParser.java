package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Summary;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by anurag on 06/03/18.
 */

public class SummaryParser {

    public Summary summaryResponseByParsingJsonResponse(JSONArray jsonArray){
        JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
        if(jsonObject == null){
            return null;
        }
        return summaryResponseByParsingJsonResponse(jsonObject);
    }

    public Summary summaryResponseByParsingJsonResponse(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        Summary summary = new Summary();
        summary.setPickUpFromWarehouse(jsonObject.optInt("pick_up_from_warehouse",0));
        summary.setReturnsFromCustomers(jsonObject.optInt("returns_from_customers",0));
        summary.setDropOffDeliveryOrdersCount(jsonObject.optInt("drop_off_delivery_orders_count",0));
        summary.setDropOffCashSales(jsonObject.optInt("drop_off_cash_sales",0));
        summary.setDropOffToWarehouse(jsonObject.optInt("drop_off_to_warehouse",0));
        summary.setInformationStocks(jsonObject.optInt("information_stocks",0));
        summary.setInformationOnHistory(jsonObject.optInt("information_on_history",0));
        summary.setInformationOnReports(jsonObject.optInt("information_on_reports",0));
        return summary;
    }
}
