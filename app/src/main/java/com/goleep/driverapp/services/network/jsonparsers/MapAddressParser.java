package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.MapAttribute;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shubham on 03/04/2018.
 */

public class MapAddressParser {
    MapAttribute mapAttribute;

    public MapAttribute reportsDataByParsingJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        mapAttribute = new MapAttribute();
        if (jsonObject.optString("status").equals("OK")) {
            JSONArray jsonArrayresults = jsonObject.optJSONArray("results");
            JSONObject jsonObjectofResult = jsonArrayresults.optJSONObject(0);
            mapAttribute.setTotalAddress(jsonObjectofResult.optString("formatted_address"));
            JSONArray jsonArrayaddresscomponents = jsonObjectofResult.optJSONArray("address_components");
            for (int index = 0; index < jsonArrayaddresscomponents.length(); index++) {
                getTypeAndSetAttributes(jsonArrayaddresscomponents.optJSONObject(index));
            }
        }

        return mapAttribute;
    }

    private void getTypeAndSetAttributes(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONArray("types");
        for (int typeLength = 0; typeLength < jsonArray.length(); typeLength++) {
            if (jsonArray.optString(typeLength).equals("street_number")) {
                mapAttribute.setAddressLine1(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("route")) {
                mapAttribute.setAddressLine1(mapAttribute.getAddressLine1() + " " + jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("sublocality_level_2")) {
                mapAttribute.setAddressLine2(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("sublocality_level_1")) {
                mapAttribute.setAddressLine2(mapAttribute.getAddressLine2() + " " + jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("administrative_area_level_2")) {
                mapAttribute.setCity(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("administrative_area_level_1")) {
                mapAttribute.setState(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("country")) {
                mapAttribute.setCountry(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("postal_code")) {
                mapAttribute.setPostalCode(jsonObject.optString("long_name"));
            }
        }
    }
}
