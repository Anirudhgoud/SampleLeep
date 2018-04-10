package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Location;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by anurag on 12/03/18.
 */

public class LocationParser {

    public int getDestinationBusinessId(JSONArray jsonArray, int destinationLocationId) {
        JSONObject firstObj = (JSONObject) jsonArray.opt(0);
        if (firstObj == null) {
            return -1;
        }
        JSONArray jsonList = firstObj.optJSONArray("data");
        if (jsonList == null) {
            return -1;
        }
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject jsonObject = jsonList.optJSONObject(i);
            int id = jsonObject.optInt("id");
            if (id == destinationLocationId) {
                return jsonObject.optInt("business_id");
            }
        }
        return 0;
    }

    public Location getBusinessLocation(JSONArray jsonArray) {
        JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
        if (jsonObject == null) {
            return null;
        }
        Location location = new Location();
        location.setId(jsonObject.optInt("id"));
        location.setAddressLine1(jsonObject.optString("address_line_1"));
        location.setAddressLine2(jsonObject.optString("address_line_2"));
        location.setCity(jsonObject.optString("city"));
        location.setState(jsonObject.optString("state"));
        location.setCountry(jsonObject.optString("country"));
        location.setPincode(jsonObject.optString("pin_code"));
        location.setOutstandingBalance(jsonObject.optDouble("outstanding_balance", 0));
        return location;
    }

}
