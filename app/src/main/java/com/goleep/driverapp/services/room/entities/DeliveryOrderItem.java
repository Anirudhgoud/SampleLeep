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
public class DeliveryOrderItem extends BaseListItem{

    @PrimaryKey
    private int id;
    private int doId;
    private int quantity;
    @Embedded
    private Product product;
    private Double price;

    public DeliveryOrderItem(){

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
