package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Business;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 04/04/2018.
 */

public class BusinessDataParser {
    public Business customerBusinessDataByparsingJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        Business business = new Business();
        business.setId(jsonObject.optInt("id"));
        business.setName(jsonObject.optString("name"));
        return business;
    }

    public List<Business> businessCategoryDataByParsingJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        } else {
            List<Business> listBusinessData = new ArrayList<>();
            JSONArray jsonArrayData = jsonObject.optJSONArray("data");
            for (int index = 0; index < jsonArrayData.length(); index++) {
                JSONObject jsonObject1 = jsonArrayData.optJSONObject(index);
                Business business = new Business();
                business.setId(jsonObject1.optInt("id"));
                business.setName(jsonObject1.optString("name"));
                listBusinessData.add(business);
            }
            return listBusinessData;
        }
    }
}
