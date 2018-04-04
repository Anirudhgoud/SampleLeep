package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.BusinessAttribute;
import com.goleep.driverapp.helpers.uimodels.GetBusinessesData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 04/04/2018.
 */

public class GetBusinessesDataParser {
    public List<GetBusinessesData> reportsDataByParsingJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        List<GetBusinessesData>  listGetBusinessesData = new ArrayList<>();
        JSONArray jsonArray = jsonObject.optJSONArray("data");
        for(int index =0 ;index < jsonArray.length();index++){
            JSONObject  jsonObject1 = jsonArray.optJSONObject(index);
            GetBusinessesData getBusinessesData = new GetBusinessesData();
            getBusinessesData.setId(jsonObject1.optInt("id"));
            getBusinessesData.setName(jsonObject1.optString("name"));
            getBusinessesData.setImageUrl(jsonObject1.optString("image_url"));
            getBusinessesData.setCategory(jsonObject1.optString("category"));
            getBusinessesData.setLocationsCount(jsonObject1.optInt("locations_count"));
            getBusinessesData.setActive(jsonObject1.optBoolean("active"));
            listGetBusinessesData.add(getBusinessesData);
        }
        return  listGetBusinessesData;
    }
}
