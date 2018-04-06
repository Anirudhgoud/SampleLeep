package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Business;

import org.json.JSONObject;

/**
 * Created by shubham on 04/04/2018.
 */

public class BusinessDataParser {
    public Business reportsDataByParsingJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        Business business = new Business();
        business.setId(jsonObject.optInt("id"));
        business.setName(jsonObject.optString("name"));
        return business;
    }
}
