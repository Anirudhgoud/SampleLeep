package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.ReturnReason;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 05/04/18.
 */

public class ReturnReasonParser {

    public List<ReturnReason> parseJsonForReturnReasons(JSONArray jsonArray){
        JSONObject firstObj = jsonArray.optJSONObject(0);
        if(firstObj == null){
            return null;
        }
        return parseJsonForReturnReasons(firstObj);
    }

    private List<ReturnReason> parseJsonForReturnReasons(JSONObject firstObj) {
        if(firstObj == null){
            return null;
        }
        List<ReturnReason> returnReasons = new ArrayList<>();
        JSONArray dataJsonArray = firstObj.optJSONArray("data");
        if(dataJsonArray == null)
            return  null;
        int length = dataJsonArray.length();
        for(int i=0;i<length;i++){
            JSONObject returnReasonJson = dataJsonArray.optJSONObject(i);
            ReturnReason returnReason = new ReturnReason();
            returnReason.setId(returnReasonJson.optInt("id"));
            returnReason.setReason(returnReasonJson.optString("reason"));
            returnReason.setReasonCategory(returnReasonJson.optString("category"));
            returnReasons.add(returnReason);
        }
        return returnReasons;
    }
}
