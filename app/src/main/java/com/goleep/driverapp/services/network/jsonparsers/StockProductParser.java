package com.goleep.driverapp.services.network.jsonparsers;

import com.goleep.driverapp.services.room.entities.StockProductEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 19/03/18.
 */

public class StockProductParser {

    public List<StockProductEntity> getStockProduct(JSONArray response){
        List<StockProductEntity> stockProductEntities = new ArrayList<>();
        JSONObject firstObject = response.optJSONObject(0);
        JSONArray dataJson = firstObject.optJSONArray("data");
        int length = dataJson.length();
        for(int i=0;i<length;i++){
            JSONObject productJson = dataJson.optJSONObject(i);
            StockProductEntity stockProductEntity = new StockProductEntity();
            stockProductEntity.setId(productJson.optInt("id"));
            stockProductEntity.setSku(productJson.optString("sku"));
            stockProductEntity.setCategory(productJson.optString("category"));
            stockProductEntity.setDefaultPrice(productJson.optDouble("default_price", 0.0));
            stockProductEntity.setProductName(productJson.optString("product_name"));
            stockProductEntity.setDeliverableQuantity(productJson.optInt("deliverable_quantity"));
            stockProductEntity.setSellableQuantity(productJson.optInt("sellable_quantity"));
            stockProductEntity.setReturnableQuantity(productJson.optInt("returnable_quantity"));
            stockProductEntity.setBarcode(productJson.optString("barcode"));
            stockProductEntity.setWeight(productJson.optString("weight"));
            stockProductEntity.setWeightUnit(productJson.optString("weight_unit"));
            stockProductEntities.add(stockProductEntity);
        }
        return stockProductEntities;
    }
}
