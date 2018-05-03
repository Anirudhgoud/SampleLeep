package com.tracotech.tracoman.helpers.uimodels;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private int id;
    private String productName;
    private double price;
    private int quantity;
    private int maxQuantity;
    private int returnQuantity;
    private String weight;
    private String weightUnit;
    private ReturnReason returnReason;

    public Product(){
    }

    //Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

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

    public int getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(int returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public ReturnReason getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(ReturnReason returnReason) {
        this.returnReason = returnReason;
    }

    public double getTotalPrice(){
        return price * quantity;
    }

    public double getTotalReturnsPrice(){
        return price * returnQuantity;
    }

    //Parcel
    protected Product(Parcel in) {
        id = in.readInt();
        productName = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        maxQuantity = in.readInt();
        returnQuantity = in.readInt();
        weight = in.readString();
        weightUnit = in.readString();
        returnReason = (ReturnReason) in.readValue(ReturnReason.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(productName);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeInt(maxQuantity);
        dest.writeInt(returnQuantity);
        dest.writeString(weight);
        dest.writeString(weightUnit);
        dest.writeValue(returnReason);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
