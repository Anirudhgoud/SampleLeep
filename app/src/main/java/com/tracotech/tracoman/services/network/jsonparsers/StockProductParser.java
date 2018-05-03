package com.tracotech.tracoman.services.network.jsonparsers;

import com.tracotech.tracoman.services.room.entities.StockProductEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishalm on 19/03/18.
 */

public class StockProductParser {

    public List<StockProductEntity> getStockProducts(JSONArray response) {
        List<StockProductEntity> stockProductEntities = new ArrayList<>();
        JSONObject firstObject = response.optJSONObject(0);
        if(firstObject == null)
            return  null;

        JSONArray dataJson = firstObject.optJSONArray("data");
        if(dataJson == null)
            return null;

        int length = dataJson.length();
        for (int i = 0; i < length; i++) {
            JSONObject productJson = dataJson.optJSONObject(i);
            StockProductEntity stockProductEntity = new StockProductEntity();
            stockProductEntity.setId(productJson.optInt("id"));
            stockProductEntity.setSku(productJson.optString("sku"));
            stockProductEntity.setCategory(productJson.optString("category"));
            stockProductEntity.setDefaultPrice(productJson.optDouble("default_price", 0.0));
            stockProductEntity.setProductName(productJson.optString("product_name"));
            int deliverableQuantity = productJson.optInt("deliverable_quantity");
            stockProductEntity.setDeliverableQuantity(deliverableQuantity);
            stockProductEntity.setMaxDeliverableQuantity(deliverableQuantity);
            int sellableQuantity = productJson.optInt("sellable_quantity");
            stockProductEntity.setSellableQuantity(sellableQuantity);
            stockProductEntity.setMaxSellableQuantity(sellableQuantity);
            int returnableQuantity = productJson.optInt("returnable_quantity");
            stockProductEntity.setReturnableQuantity(returnableQuantity);
            stockProductEntity.setMaxReturnableQuantity(returnableQuantity);
            stockProductEntity.setBarcode(productJson.optString("barcode"));
            stockProductEntity.setWeight(productJson.optString("weight"));
            stockProductEntity.setWeightUnit(productJson.optString("weight_unit"));
            stockProductEntities.add(stockProductEntity);
        }
        return stockProductEntities;
    }

    public List<StockProductEntity> productListByParsingJsonResponse(JSONArray jsonResponse) {
        List<StockProductEntity> productList = new ArrayList<>();
        if (jsonResponse == null) return productList;
        JSONObject firstObj = (JSONObject) jsonResponse.opt(0);
        if (firstObj == null) return productList;
        JSONArray jsonArray = firstObj.optJSONArray("data");
        if (jsonArray == null) return productList;
        int listCount = jsonArray.length();
        for (int i = 0; i < listCount; i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            StockProductEntity product = productByParsingJsonObject(jsonObject);
            if (product != null) productList.add(product);
        }
        return productList;
    }


    private StockProductEntity productByParsingJsonObject(JSONObject jsonObject) {
        if (jsonObject == null) return null;
        StockProductEntity product = new StockProductEntity();
        product.setId(jsonObject.optInt("id"));
        product.setProductName(jsonObject.optString("name"));
        product.setWeight(jsonObject.optString("weight"));
        product.setWeightUnit(jsonObject.optString("weight_unit"));
        int quantity = jsonObject.optInt("returnable_quantity", -1);
        product.setReturnableQuantity(quantity);
        product.setMaxReturnableQuantity(quantity);
        product.setDefaultPrice(jsonObject.optDouble("price"));
        return product;
    }
}
