package com.goleep.driverapp.services.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.goleep.driverapp.services.network.responsemodels.DoDetailResponseModel;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by vishalm on 28/02/18.
 */
@Entity
public class Product implements Parcelable {
    @PrimaryKey
    private Integer id;
    private String name;
    private String sku;
    private String weight;
    private String weightUnit;

    public Product(Integer id, String name, String sku, String weight, String weightUnit, Integer doId, Integer doItemId) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.doId = doId;
        this.doItemId = doItemId;
    }

    public Integer getDoId() {
        return doId;
    }

    public void setDoId(Integer doId) {
        this.doId = doId;
    }

    public Integer getDoItemId() {
        return doItemId;
    }

    public void setDoItemId(Integer doItemId) {
        this.doItemId = doItemId;
    }

    private Integer doId;
    private Integer doItemId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public static List<Product> getProductsList(DoDetailResponseModel doDetailResponseModel) {
        List<Product> products = new ArrayList<>();
        List<DoDetailResponseModel.DeliveryOrderItem> deliveryOrderItems = doDetailResponseModel.getDeliveryOrderItems();
        for(int i=0;i<deliveryOrderItems.size();i++){
            DoDetailResponseModel.DeliveryOrderItem responseDoItem = deliveryOrderItems.get(i);
            DoDetailResponseModel.Product responseProduct = responseDoItem.getProduct();
            Product product = new Product(responseProduct.getId(), responseProduct.getName(),
                    responseProduct.getSku(), responseProduct.getWeight(), responseProduct.getWeightUnit(),
                    doDetailResponseModel.getId(), responseDoItem.getId());
            products.add(product);
        }
        return products;
    }

    protected Product(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        name = in.readString();
        sku = in.readString();
        weight = in.readString();
        weightUnit = in.readString();
        doId = in.readByte() == 0x00 ? null : in.readInt();
        doItemId = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(sku);
        dest.writeString(weight);
        dest.writeString(weightUnit);
        if (doId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(doId);
        }
        if (doItemId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(doItemId);
        }
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