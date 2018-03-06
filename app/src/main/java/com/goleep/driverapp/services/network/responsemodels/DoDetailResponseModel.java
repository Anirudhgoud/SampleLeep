package com.goleep.driverapp.services.network.responsemodels;

import com.goleep.driverapp.helpers.uimodels.DeliveryOrderItemUIModel;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 01/03/18.
 */

public class DoDetailResponseModel {
    private Integer doID;

    private List<DeliveryOrderItem> deliveryOrderItems = new ArrayList<>();

    public Integer getId() {
        return doID;
    }

    public void setId(Integer doID) {
        this.doID = doID;
    }


    public List<DeliveryOrderItem> getDeliveryOrderItems() {
        return deliveryOrderItems;
    }

    public void setDeliveryOrderItems(List<DeliveryOrderItem> deliveryOrderItems) {
        this.deliveryOrderItems = deliveryOrderItems;
    }

    public void parseJSON(JSONArray itemsArray, Integer doId){
        this.doID = doId;
        List<DeliveryOrderItem> deliveryOrderItems = new ArrayList<>();
        for(int i=0;i<itemsArray.length();i++){
            JSONObject itemJSON = itemsArray.optJSONObject(i);
            Integer id = itemJSON.optInt("id");
            Product product = parseProduct(itemJSON.optJSONObject("product"));
            Integer quantity = itemJSON.optInt("quantity");
            Double price = itemJSON.optDouble("price");
            DeliveryOrderItem item = new DeliveryOrderItem();
            item.setId(id);
            item.setDoId(doId);
            item.setPrice(price);
            item.setProduct(product);
            item.setQuantity(quantity);
            deliveryOrderItems.add(item);
        }
        this.deliveryOrderItems = deliveryOrderItems;
    }

    private Product parseProduct(JSONObject productJSON) {
        Integer productId = productJSON.optInt("id");
        String name = productJSON.optString("name");
        String sku = productJSON.optString("sku");
        String weight = productJSON.optString("weight");
        String weightUnit = productJSON.optString("weight_unit");
        Product product = new Product();
        product.setProductId(productId);
        product.setName(name);
        product.setSku(sku);
        product.setWeight(weight);
        product.setWeightUnit(weightUnit);
        return product;
    }
}
