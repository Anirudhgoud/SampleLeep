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

    public DeliveryOrderItem(Integer id, Integer doId, Integer quantity, Product product, Integer price) {
        super(0);
        this.id = id;
        this.doId = doId;
        this.quantity = quantity;
        this.product = product;
        this.price = price;
    }

    public Integer getDoId() {
        return doId;
    }

    public void setDoId(Integer doId) {
        this.doId = doId;
    }

    private Integer doId;
    private Integer quantity;

    @Embedded
    private Product product;
    private Integer price;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public static List<DeliveryOrderItem> getDeliveryOrderItemList(DoDetailResponseModel doDetailResponseModel) {
        List<DeliveryOrderItem> deliveryOrderItemList = new ArrayList<>();
        List<DoDetailResponseModel.DeliveryOrderItem> deliveryOrderItems = doDetailResponseModel.getDeliveryOrderItems();
        for(int i=0;i<deliveryOrderItems.size();i++){
            DoDetailResponseModel.DeliveryOrderItem responseDoItem = deliveryOrderItems.get(i);
            DoDetailResponseModel.Product responseProduct = responseDoItem.getProduct();
            Product doProduct = new Product(responseProduct.getId(), responseProduct.getName(),
                    responseProduct.getSku(), responseProduct.getWeight(), responseProduct.getWeightUnit());
            DeliveryOrderItem deliveryOrderItem =  new DeliveryOrderItem(responseDoItem.getId(),
                    doDetailResponseModel.getId(), responseDoItem.getQuantity(),
                    doProduct, responseDoItem.getPrice());
            deliveryOrderItemList.add(deliveryOrderItem);
        }
        return deliveryOrderItemList;
    }


}
