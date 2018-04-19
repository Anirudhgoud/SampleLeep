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
    public List<Country> reportsDataByParsingJsonResponse(JSONArray jsonArray) {
        List<Country> liscountryAttribute = new ArrayList<>();
        if (jsonArray == null) {
            return null;
        }
        int jsonArrayLength = jsonArray.length();
        for (int index = 0; index < jsonArrayLength; index++) {
            JSONObject jsonObject1 = jsonArray.optJSONObject(index);
            Country country = new Country();
            country.setId(jsonObject1.optInt("id"));
            country.setName(jsonObject1.optString("name"));
            country.setDialCode(jsonObject1.optString("country_code"));
            country.setCurrencySymbol(jsonObject1.optString("currency_symbol"));
            liscountryAttribute.add(country);
        }
        return liscountryAttribute;
    }
}
