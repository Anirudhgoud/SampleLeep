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
    public Business customerBusinessDataByParsingJsonResponse(JSONArray response) {
        JSONObject jsonObject = response.optJSONObject(0);
        if (jsonObject == null) {
            return null;
        }
        Business business = new Business();
        business.setId(jsonObject.optInt("id"));
        business.setName(jsonObject.optString("name"));
        return business;
    }

    public List<Business> businessCategoryDataByParsingJsonResponse(JSONArray response) {

        JSONObject jsonObject = response.optJSONObject(0);

        if (jsonObject == null) {
            return null;
        }
        List<Business> listBusinessData = new ArrayList<>();
        JSONArray jsonArrayData = jsonObject.optJSONArray("data");
        if(jsonArrayData == null) return null;
        int jsonArrayLength = jsonArrayData.length();
        for (int index = 0; index < jsonArrayLength; index++) {
            JSONObject jsonObject1 = jsonArrayData.optJSONObject(index);
            Business business = new Business();
            business.setId(jsonObject1.optInt("id"));
            business.setName(jsonObject1.optString("name"));
            listBusinessData.add(business);
        }
        return listBusinessData;
    }
}
