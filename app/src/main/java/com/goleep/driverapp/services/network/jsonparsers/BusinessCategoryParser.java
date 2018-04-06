package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Business;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 03/04/2018.
 */

public class BusinessCategoryParser {
    public List<Business> reportsDataByParsingJsonResponse(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }else {
                List<Business> listBusinessData = new ArrayList<>();
            JSONArray jsonArrayData = jsonObject.optJSONArray("data");
            for(int index = 0; index<jsonArrayData.length();index++){
                JSONObject jsonObject1 = jsonArrayData.optJSONObject(index);
                Business business =new Business();
                business.setId(jsonObject1.optInt("id"));
                business.setName(jsonObject1.optString("name"));
                listBusinessData.add(business);
            }
            return  listBusinessData;
        }
    }

}
