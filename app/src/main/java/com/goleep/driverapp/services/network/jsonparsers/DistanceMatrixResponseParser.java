package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Distance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 16/03/18.
 */

public class DistanceMatrixResponseParser {

    public List<Distance> parseDistanceMatrixResponse(JSONObject response) {
        List<Distance> timeToReach = new ArrayList<>();
        if(response.optString("status", "").equalsIgnoreCase("ok")){
            JSONArray elements = response.optJSONArray("rows").
                    optJSONObject(0).optJSONArray("elements");
            for(int i=0;i< elements.length();i++){
                JSONObject durationObject = (JSONObject) elements.opt(i);
                Distance distance = new Distance();
                distance.setDistanceText(durationObject.optJSONObject("distance").optString("text"));
                distance.setDistanceValue(durationObject.optJSONObject("distance").optInt("value"));
                distance.setDurationText(durationObject.optJSONObject("duration").optString("text"));
                distance.setDurationValue(durationObject.optJSONObject("duration").optInt("value"));
                timeToReach.add(distance);
            }
        }
        return timeToReach;
    }
}
