package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Business;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 04/04/2018.
 */

public class GetBusinessesDataParser {
    public List<Business> reportsDataByParsingJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        List<Business>  listGetBusinessesData = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray("data");
        for(int index =0 ;index < jsonArray.length();index++){
            JSONObject  jsonObject1 = jsonArray.optJSONObject(index);
            Business business = new Business();
            business.setId(jsonObject1.optInt("id"));
            business.setName(jsonObject1.optString("name"));
            listGetBusinessesData.add(business);
        }
        return  listGetBusinessesData;
    }
}
