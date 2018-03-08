package com.goleep.driverapp.services.room.entities;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by vishalm on 27/02/18.
 */
@Entity(foreignKeys = @ForeignKey(entity = DeliveryOrderEntity.class,
        parentColumns = "id",
        childColumns = "doId",
        onDelete = CASCADE))
public class OrderItemEntity extends BaseListItem{

    @PrimaryKey
    private int id;
    private int doId;
    private int quantity;
    private int maxQuantity;
    @Embedded
    private ProductEntity product;
    private double price;

    public OrderItemEntity(){

    }

    public int getDoId() {
        return doId;
    }

    public void setDoId(int doId) {
        this.doId = doId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
}
