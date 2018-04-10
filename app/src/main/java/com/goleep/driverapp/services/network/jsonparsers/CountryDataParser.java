package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Country;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 05/04/2018.
 */

public class CountryDataParser {
    public List<Country> reportsDataByParsingJsonResponse(JSONObject jsonObject) {
        List<Country> liscountryAttribute = new ArrayList<>();
        if (jsonObject == null) {
            return null;
        }
        JSONArray jsonArraydata = jsonObject.optJSONArray("data");
        int jsonArrayLength = jsonArraydata.length();
        for (int index = 0; index < jsonArrayLength; index++) {
            JSONObject jsonObject1 = jsonArraydata.optJSONObject(index);
            Country country = new Country();
            country.setId(jsonObject1.optInt("id"));
            country.setName(jsonObject1.optString("name"));
            liscountryAttribute.add(country);
        }
        return liscountryAttribute;
    }
}
