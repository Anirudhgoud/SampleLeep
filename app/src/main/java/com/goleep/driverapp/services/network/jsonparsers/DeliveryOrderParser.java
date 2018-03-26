package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 06/03/18.
 */

public class DeliveryOrderParser {

    
    public List<DeliveryOrderEntity> deliveryOrdersByParsingJsonResponse(JSONArray jsonArray){
        List<DeliveryOrderEntity> deliveryOrders = new ArrayList<>();
        JSONObject firstObj = (JSONObject) jsonArray.opt(0);
        if(firstObj == null){
            return deliveryOrders;
        }
        JSONArray jsonList = firstObj.optJSONArray("data");
        if(jsonList == null){
            return deliveryOrders;
        }
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject jsonObject = jsonList.optJSONObject(i);
            DeliveryOrderEntity deliveryOrder = deliveryOrderByParsingJsonResponse(jsonObject);
            if(deliveryOrder != null){
                deliveryOrders.add(deliveryOrder);
            }
        }
        return deliveryOrders;
    }
    
    
    public DeliveryOrderEntity deliveryOrderByParsingJsonResponse(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        DeliveryOrderEntity deliveryOrder = new DeliveryOrderEntity();
        deliveryOrder.setId(jsonObject.optInt("id"));
        deliveryOrder.setDoNumber(jsonObject.optString("do_number"));
        deliveryOrder.setStatus(jsonObject.optString("status"));
        deliveryOrder.setType(jsonObject.optString("type"));
        deliveryOrder.setSourceLocationId(jsonObject.optInt("source_location_id"));
        deliveryOrder.setPreferredDeliveryDate(jsonObject.optString("preferred_delivery_date"));
        deliveryOrder.setActualDeliveryDate(jsonObject.optString("actual_delivery_date"));
        deliveryOrder.setPreferredDeliveryTime(jsonObject.optString("preferred_delivery_time"));
        deliveryOrder.setSourceLocationName(jsonObject.optString("source_location_name"));
        deliveryOrder.setDestinationLocationId(jsonObject.optInt("destination_location_id"));
        deliveryOrder.setDestinationLocationName(jsonObject.optString("destination_location_name"));
        deliveryOrder.setCreatedAt(jsonObject.optString("created_at"));
        deliveryOrder.setAssigneeName(jsonObject.optString("assignee_name"));
        deliveryOrder.setCustomerName(jsonObject.optString("customer_name"));
        deliveryOrder.setDestinationLatitude(jsonObject.optDouble("destination_latitude", 0.0));
        deliveryOrder.setDestinationLongitude(jsonObject.optDouble("destination_longitude", 0.0));
        deliveryOrder.setDestinationAddressLine1(jsonObject.optString("destination_address_line_1"));
        deliveryOrder.setDestinationAddressLine2(jsonObject.optString("destination_address_line_2"));
        deliveryOrder.setDeliveryOrderItemsCount(jsonObject.optInt("delivery_order_items_count", 0));
        deliveryOrder.setTotalValue((float)jsonObject.optDouble("total_value", 0));
        return  deliveryOrder;
    }
}
