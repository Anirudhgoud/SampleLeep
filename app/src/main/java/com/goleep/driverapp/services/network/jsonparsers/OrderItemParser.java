package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.helpers.uimodels.ReturnOrderItem;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ProductEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 06/03/18.
 */

public class OrderItemParser {

    public List<OrderItemEntity> orderItemsByParsingJsonResponse(JSONArray jsonArray, int deliveryOrderId){
        List<OrderItemEntity> orderItems = new ArrayList<>();
        JSONObject firstObj = (JSONObject) jsonArray.opt(0);
        if(firstObj == null){
            return orderItems;
        }
        JSONArray jsonList = firstObj.optJSONArray("delivery_order_items");
        if(jsonList == null){
            return orderItems;
        }
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject jsonObject = jsonList.optJSONObject(i);
            OrderItemEntity orderItem = orderItemByParsingJsonResponse(jsonObject, deliveryOrderId);
            if(orderItem != null){
                orderItems.add(orderItem);
            }
        }
        return orderItems;
    }


    public List<ReturnOrderItem> returnOrderItemsByParsingJsonResponse(JSONArray jsonArray, int roNumber){
        List<ReturnOrderItem> orderItems = new ArrayList<>();
        JSONObject firstObj = jsonArray.optJSONObject(0);
        if(firstObj == null){
            return orderItems;
        }
        JSONArray jsonList = firstObj.optJSONArray("return_order_items");
        if(jsonList == null){
            return orderItems;
        }
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject jsonObject = jsonList.optJSONObject(i);
            ReturnOrderItem orderItem = returnOrderItemByParsingJsonResponse(jsonObject, roNumber);
            if(orderItem != null){
                orderItems.add(orderItem);
            }
        }
        return orderItems;
    }

    private ReturnOrderItem returnOrderItemByParsingJsonResponse(JSONObject jsonObject, int roNumber) {
        if(jsonObject == null){
            return null;
        }
        ReturnOrderItem returnOrderItem = new ReturnOrderItem();
        returnOrderItem.setId(jsonObject.optInt("id"));
        returnOrderItem.setOrderId(roNumber);
        returnOrderItem.setPrice(jsonObject.optDouble("price", 0));
        returnOrderItem.setProduct(productByParsingJsonResponse(jsonObject.optJSONObject("product")));
        returnOrderItem.setQuantity(jsonObject.optInt("quantity", 0));
        returnOrderItem.setReason(jsonObject.optJSONObject("return_reason").optString("reason"));
        return returnOrderItem;
    }


    public OrderItemEntity orderItemByParsingJsonResponse(JSONObject jsonObject, int deliveryOrderId){
        if(jsonObject == null){
            return null;
        }
        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setId(jsonObject.optInt("id"));
        orderItem.setOrderId(deliveryOrderId);
        orderItem.setPrice(jsonObject.optDouble("price", 0));
        orderItem.setProduct(productByParsingJsonResponse(jsonObject.optJSONObject("product")));
        orderItem.setQuantity(jsonObject.optInt("quantity", 0));
        orderItem.setMaxQuantity(orderItem.getQuantity());
        orderItem.setSelected(true);
        return orderItem;
    }

    public ProductEntity productByParsingJsonResponse(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        ProductEntity product = new ProductEntity();
        product.setProductId(jsonObject.optInt("id"));
        product.setName(jsonObject.optString("name"));
        product.setSku(jsonObject.optString("sku"));
        product.setWeight(jsonObject.optString("weight"));
        product.setWeightUnit(jsonObject.optString("weight_unit"));
        return product;
    }

    public double getProductPriceByParsingJsonArray(JSONArray jsonArray){
        JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
        if(jsonObject == null){
            return 0.0;
        }
        return jsonObject.optDouble("price", 0.0);
    }


    public int getDestinationBusinessIdParsingDoDetailsJson(JSONArray jsonArray) {
        JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
        if (jsonObject == null) {
            return -1;
        }
        JSONObject destinationLocation = jsonObject.optJSONObject("destination_location");
        if (destinationLocation != null) {
            return destinationLocation.optInt("business_id");
        } else {
            return -1;
        }
    }
}
