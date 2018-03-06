package com.goleep.driverapp.helpers.uimodels;

import com.goleep.driverapp.interfaces.ParentListItem;
import com.goleep.driverapp.services.room.entities.DeliveryOrderItem;
import com.goleep.driverapp.services.room.entities.Product;

import org.json.JSONObject;

/**
 * Created by vishalm on 05/03/18.
 */

public class DeliveryOrderItemUIModel extends DeliveryOrderItem implements ParentListItem {

    private Integer itemType;

    public  DeliveryOrderItemUIModel(){

    }

//    private Product parseProduct(JSONObject response) {
//        Integer productId = response.optInt("id");
//        String name = response.optString("name");
//        String sku = response.optString("sku");
//        String weight = response.optString("weight");
//        String weightUnit = response.optString("weight_unit");
//        Product product = new Product(productId, name, sku, weight, weightUnit);
//        return product;
//    }

//    public DeliveryOrderItem parseJson(JSONObject response, Integer doId){
//        Integer id = response.optInt("id");
//        Product product = parseProduct(response.optJSONObject("product"));
//        Integer quantity = response.optInt("quantity");
//        Double price = response.optDouble("price");
//        DeliveryOrderItem item = new DeliveryOrderItem();
//        item.setId(id);
//        item.setDoId(doId);
//        item.setPrice(price);
//        item.setProduct(product);
//        item.setQuantity(quantity);
//        return item;
//    }

    @Override
    public void setType(Integer itemType) {
        this.itemType = itemType;
    }
}
