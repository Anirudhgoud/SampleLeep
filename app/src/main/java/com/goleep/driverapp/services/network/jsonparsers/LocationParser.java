package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.Address;
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
    public Location getBusinessLocation(JSONObject jsonObject) {
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

    public Address getAddressByParsingJsonResponse(JSONArray response) {
        JSONObject jsonObject =  response.optJSONObject(0);
        if (jsonObject == null) {
            return null;
        }
        Address address = new Address();
        if (jsonObject.optString("status").equals("OK")) {
            JSONArray jsonArrayresults = jsonObject.optJSONArray("results");
            JSONObject jsonObjectofResult = jsonArrayresults.optJSONObject(0);
            address.setFormattedAddress(jsonObjectofResult.optString("formatted_address"));
            JSONArray jsonArrayaddresscomponents = jsonObjectofResult.optJSONArray("address_components");
            for (int index = 0; index < jsonArrayaddresscomponents.length(); index++) {
                getTypeAndSetAttributes(address,jsonArrayaddresscomponents.optJSONObject(index));
            }
        }
        return address;
    }

    private void getTypeAndSetAttributes(Address address ,JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.optJSONArray("types");
        int jsonArryLenght = jsonArray.length();
        for (int index = 0; index < jsonArryLenght; index++) {
            if (jsonArray.optString(index).equals("street_number")) {
                address.setAddressLine1(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(index).equals("route")) {
                address.setAddressLine1(address.getAddressLine1() + " " + jsonObject.optString("long_name"));
            } else if (jsonArray.optString(index).equals("sublocality_level_2")) {
                address.setAddressLine2(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(index).equals("sublocality_level_1")) {
                address.setAddressLine2(address.getAddressLine2() + " " + jsonObject.optString("long_name"));
            } else if (jsonArray.optString(index).equals("administrative_area_level_2")) {
                address.setCity(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(index).equals("administrative_area_level_1")) {
                address.setState(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(index).equals("country")) {
                address.setCountry(jsonObject.optString("long_name"));
            } else if (jsonArray.optString(index).equals("postal_code")) {
                address.setPostalCode(jsonObject.optString("long_name"));
            }
        }
    }

}
