package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.services.room.entities.DriverEntity;
import com.goleep.driverapp.services.room.entities.WarehouseEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 06/03/18.
 */

public class DriverDataParser {

    public DriverEntity driverResponseByParsingJsonResponse(JSONArray jsonArray){
        JSONObject firstObj = jsonArray.optJSONObject(0);
        if(firstObj == null){
            return null;
        }
        return driverByParsingJsonResponse(firstObj);
    }

    public DriverEntity driverByParsingJsonResponse(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        DriverEntity driverEntity = new DriverEntity();
        driverEntity.setId(jsonObject.optInt("id"));
        driverEntity.setFirstName(jsonObject.optString("first_name"));
        driverEntity.setLastName(jsonObject.optString("last_name"));
        driverEntity.setImageUrl(jsonObject.optString("image_url"));
        driverEntity.setEmail(jsonObject.optString("email"));
        driverEntity.setLicenceType(jsonObject.optString("licence_type"));
        driverEntity.setAge(jsonObject.optInt("age"));
        driverEntity.setExperience(jsonObject.optString("experience"));
        driverEntity.setInsurance(jsonObject.optBoolean("insurance"));
        driverEntity.setDateOfBirth(jsonObject.optString("date_of_birth"));
        driverEntity.setContactNumber(jsonObject.optString("contact_number"));
        driverEntity.setLicenceNumber(jsonObject.optString("licence_number"));
        driverEntity.setVehicleNumber(jsonObject.optString("vehicle_number"));
        driverEntity.setLicenceExpirationDate(jsonObject.optString("licence_expiration_date"));
        driverEntity.setCompletedDeliveryOrdersCount(jsonObject.optInt("completed_delivery_orders_count", 0));
        driverEntity.setPaymentCollected(jsonObject.optInt("payment_collected", 0));
        driverEntity.setDeliveryLocationsCount(jsonObject.optInt("delivery_locations_count", 0));
        JSONObject workLocationJsonObject = jsonObject.optJSONObject("work_location");
        if (workLocationJsonObject != null){
            driverEntity.setLocationId(workLocationJsonObject.optInt("id"));
            driverEntity.setAddressLine1(workLocationJsonObject.optString("address_line_1"));
            driverEntity.setAddressLine2(workLocationJsonObject.optString("address_line_2"));
            driverEntity.setCity(workLocationJsonObject.optString("city"));
            driverEntity.setState(workLocationJsonObject.optString("state"));
            driverEntity.setCountryId(workLocationJsonObject.optInt("country_id"));
            driverEntity.setCountryName(workLocationJsonObject.optString("country_name"));
            driverEntity.setPinCode(workLocationJsonObject.optString("pin_code"));
            driverEntity.setWorkLocationLat(workLocationJsonObject.optDouble("latitude", 0.0));
            driverEntity.setWorkLocationLng(workLocationJsonObject.optDouble("longitude", 0.0));
            driverEntity.setLocationName(workLocationJsonObject.optString("name"));
            driverEntity.setBusinessId(workLocationJsonObject.optInt("business_id", 0));
        }
        return driverEntity;
    }

    public List<WarehouseEntity> warehouseByParsingResponse(JSONArray response) {

        JSONObject firstObj = (JSONObject) response.opt(0);
        if(firstObj == null){
            return null;
        } else {
            List<WarehouseEntity> warehouseEntities = new ArrayList<>();
            JSONObject jsonObject = firstObj.optJSONObject("work_location");
            WarehouseEntity warehouseEntity = new WarehouseEntity();
            warehouseEntity.setId(jsonObject.optInt("id"));
            warehouseEntity.setAddressLine1(jsonObject.optString("address_line_1"));
            warehouseEntity.setAddressLine2(jsonObject.optString("address_line_2"));
            warehouseEntity.setCity(jsonObject.optString("city"));
            warehouseEntity.setState(jsonObject.optString("state"));
            warehouseEntity.setCountryName(jsonObject.optString("country_name"));
            warehouseEntity.setPincode(jsonObject.optString("pin_code"));
            warehouseEntity.setLatitude(jsonObject.optDouble("latitude", 0.0));
            warehouseEntity.setLongitude(jsonObject.optDouble("longitude", 0.0));
            warehouseEntity.setWareHouseName(jsonObject.optString("name"));
            warehouseEntities.add(warehouseEntity);
            return warehouseEntities;
        }
    }
}
