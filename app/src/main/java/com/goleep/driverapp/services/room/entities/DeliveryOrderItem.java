package com.goleep.driverapp.services.room.entities;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.services.network.responsemodels.DoDetailResponseModel;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by vishalm on 27/02/18.
 */
@Entity(foreignKeys = @ForeignKey(entity = DeliveryOrder.class,
        parentColumns = "id",
        childColumns = "doId",
        onDelete = CASCADE))
public class DeliveryOrderItem extends BaseListItem{
    @PrimaryKey
    private Integer id;
    private Integer doId;
    private Integer quantity;
    @Embedded
    private Product product;
    private Double price;

    public DeliveryOrderItem(){

    }

    public Integer getDoId() {
        return doId;
    }

    public void setDoId(Integer doId) {
        this.doId = doId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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
