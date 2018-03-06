package com.goleep.driverapp.services.network.responsemodels;

import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ProductEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 01/03/18.
 */

public class DoDetailResponseModel {
    private Integer doID;

    private List<OrderItemEntity> orderItemEntities = new ArrayList<>();

    public Integer getId() {
        return doID;
    }

    public void setId(Integer doID) {
        this.doID = doID;
    }


    public List<OrderItemEntity> getOrderItemEntities() {
        return orderItemEntities;
    }

    public void setOrderItemEntities(List<OrderItemEntity> orderItemEntities) {
        this.orderItemEntities = orderItemEntities;
    }

    public void parseJSON(JSONArray itemsArray, Integer doId){
        this.doID = doId;
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        for(int i=0;i<itemsArray.length();i++){
            JSONObject itemJSON = itemsArray.optJSONObject(i);
            Integer id = itemJSON.optInt("id");
            ProductEntity productEntity = parseProduct(itemJSON.optJSONObject("product"));
            Integer quantity = itemJSON.optInt("quantity");
            Double price = itemJSON.optDouble("price");
            OrderItemEntity item = new OrderItemEntity();
            item.setId(id);
            item.setDoId(doId);
            item.setPrice(price);
            item.setProduct(productEntity);
            item.setQuantity(quantity);
            orderItemEntities.add(item);
        }
        this.orderItemEntities = orderItemEntities;
    }

    private ProductEntity parseProduct(JSONObject productJSON) {
        Integer productId = productJSON.optInt("id");
        String name = productJSON.optString("name");
        String sku = productJSON.optString("sku");
        String weight = productJSON.optString("weight");
        String weightUnit = productJSON.optString("weight_unit");
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(productId);
        productEntity.setName(name);
        productEntity.setSku(sku);
        productEntity.setWeight(weight);
        productEntity.setWeightUnit(weightUnit);
        return productEntity;
    }
}
