package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 22/03/18.
 */

public class ReturnOrderParser {

    public List<ReturnOrderEntity> parserReturnOrderResponse(JSONArray jsonArray){
        List<ReturnOrderEntity> returnOrderEntities = new ArrayList<>();
        JSONObject firstObj = (JSONObject) jsonArray.opt(0);
        if(firstObj != null){
            JSONArray dataArray = firstObj.optJSONArray("data");
            int length = dataArray.length();
            for(int i=0;i<length;i++){
                JSONObject jsonObject = dataArray.optJSONObject(i);
                ReturnOrderEntity returnOrderEntity = parseReturnOrder(jsonObject);
                if(returnOrderEntity != null)
                    returnOrderEntities.add(returnOrderEntity);
            }
        }
        return  returnOrderEntities;
    }

    private ReturnOrderEntity parseReturnOrder(JSONObject jsonObject) {
        long roNumber = jsonObject.optLong("ro_number", -1);
        if(roNumber != -1) {
            ReturnOrderEntity returnOrderEntity = new ReturnOrderEntity();
            returnOrderEntity.setRoNumber(roNumber);
            returnOrderEntity.setAssigneeName(jsonObject.optString("assignee_name"));
            returnOrderEntity.setStatus(jsonObject.optString("status"));
            returnOrderEntity.setType(jsonObject.optString("type"));
            returnOrderEntity.setCreatedAt(jsonObject.optString("created_at"));
            returnOrderEntity.setSourceLocationName(jsonObject.optString("source_location_name"));
            returnOrderEntity.setDestinationLocationName(jsonObject.optString("destination_location_name"));
            returnOrderEntity.setReturnOrderItemsCount(jsonObject.optInt("return_order_items_count"));
            returnOrderEntity.setTotalValue(jsonObject.optDouble("total_value"));
            returnOrderEntity.setSourceLocationId(jsonObject.optInt("source_location_id"));
            returnOrderEntity.setDestinationLocationId(jsonObject.optInt("destination_location_id"));
            returnOrderEntity.setSourceAddressLine1(jsonObject.optString("source_address_line_1"));
            returnOrderEntity.setSourceAddressLine2(jsonObject.optString("source_address_line_2"));
            returnOrderEntity.setCustomerName(jsonObject.optString("customer_name"));
            returnOrderEntity.setActualReturnAt(jsonObject.optString("actual_returned_at"));
            return returnOrderEntity;
        }
        return null;
    }

    public long parseForRoNumber(JSONArray jsonArray){
        long roNumber = -1;
        if(jsonArray != null){
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            if(jsonObject != null)
                roNumber = jsonObject.optLong("ro_number", -1);
        }
        return roNumber;
    }

}
