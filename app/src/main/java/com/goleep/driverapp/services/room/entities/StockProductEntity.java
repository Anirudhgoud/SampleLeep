package com.goleep.driverapp.services.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by vishalm on 19/03/18.
 */
@Entity
public class StockProductEntity {

    @PrimaryKey
    private int id;
    private String sku;
    private String productName;
    private String category;
    private double defaultPrice;
    private int sellableQuantity;
    private int deliverableQuantity;
    private int returnableQuantity;
    private String weight;
    private String weightUnit;
    private String barcode;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public int getSellableQuantity() {
        return sellableQuantity;
    }

    public void setSellableQuantity(int sellableQuantity) {
        this.sellableQuantity = sellableQuantity;
    }

    public int getDeliverableQuantity() {
        return deliverableQuantity;
    }

    public void setDeliverableQuantity(int deliverableQuantity) {
        this.deliverableQuantity = deliverableQuantity;
    }

    public int getReturnableQuantity() {
        return returnableQuantity;
    }

    public void setReturnableQuantity(int returnableQuantity) {
        this.returnableQuantity = returnableQuantity;
    }
}
