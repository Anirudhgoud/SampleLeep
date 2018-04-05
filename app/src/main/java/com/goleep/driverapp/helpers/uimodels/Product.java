package com.goleep.driverapp.helpers.uimodels;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private int id;
    private String productName;
    private double price;
    private int quantity;
    private int maxQuantity;
    private String weight;
    private String weightUnit;

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

    //Parcel
    protected Product(Parcel in) {
        id = in.readInt();
        productName = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
        maxQuantity = in.readInt();
        weight = in.readString();
        weightUnit = in.readString();
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
        dest.writeString(weight);
        dest.writeString(weightUnit);
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
