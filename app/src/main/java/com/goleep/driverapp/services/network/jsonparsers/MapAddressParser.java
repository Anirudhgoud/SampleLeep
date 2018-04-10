package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.MapData;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shubham on 03/04/2018.
 */

public class MapAddressParser {
    MapData mapData;

    public MapData reportsDataByParsingJsonResponse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        mapData = new MapData();
        if (jsonObject.optString("status").equals("OK")) {
            JSONArray jsonArrayresults = jsonObject.optJSONArray("results");
            JSONObject jsonObjectofResult = jsonArrayresults.optJSONObject(0);
            mapData.setTotalAddress(jsonObjectofResult.optString("formatted_address"));
            JSONArray jsonArrayaddresscomponents = jsonObjectofResult.optJSONArray("address_components");
            for (int index = 0; index < jsonArrayaddresscomponents.length(); index++) {
                getTypeAndSetAttributes(jsonArrayaddresscomponents.optJSONObject(index));
            }
        }

        return mapData;
    }

    private void getTypeAndSetAttributes(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONArray("types");
        for (int typeLength = 0; typeLength < jsonArray.length(); typeLength++) {
            if (jsonArray.optString(typeLength).equals("street_number")) {
                mapData.setAddressLine1(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("route")) {
                mapData.setAddressLine1(mapData.getAddressLine1() + " " + jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("sublocality_level_2")) {
                mapData.setAddressLine2(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("sublocality_level_1")) {
                mapData.setAddressLine2(mapData.getAddressLine2() + " " + jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("administrative_area_level_2")) {
                mapData.setCity(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("administrative_area_level_1")) {
                mapData.setState(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("country")) {
                mapData.setCountry(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(typeLength).equals("postal_code")) {
                mapData.setPostalCode(jsonObject.optString("long_name"));
            }
        }
    }
}
