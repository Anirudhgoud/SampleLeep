package com.goleep.driverapp.services.room.entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.goleep.driverapp.services.network.responsemodels.DoDetailResponseModel;
import com.goleep.driverapp.services.room.entities.DeliveryOrder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
public class DeliveryOrderItem extends BaseListItem implements Parcelable {
    @PrimaryKey
    private Integer id;

    public DeliveryOrderItem(Integer id, Integer doId, Integer quantity, Integer productId, Integer price) {
        super(0);
        this.id = id;
        this.doId = doId;
        this.quantity = quantity;
        this.productId = productId;
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

    private Integer productId;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public static List<DeliveryOrderItem> getDeliveryOrderItemList(DoDetailResponseModel doDetailResponseModel) {
        List<DeliveryOrderItem> deliveryOrderItemList = new ArrayList<>();
        List<DoDetailResponseModel.DeliveryOrderItem> deliveryOrderItems = doDetailResponseModel.getDeliveryOrderItems();
        for(int i=0;i<deliveryOrderItems.size();i++){
            DoDetailResponseModel.DeliveryOrderItem responseDoItem = deliveryOrderItems.get(i);
            DeliveryOrderItem deliveryOrderItem =  new DeliveryOrderItem(responseDoItem.getId(),
                    doDetailResponseModel.getId(), responseDoItem.getQuantity(),
                    responseDoItem.getProduct().getId(), responseDoItem.getPrice());
            deliveryOrderItemList.add(deliveryOrderItem);
        }
        return deliveryOrderItemList;
    }

    protected DeliveryOrderItem(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        doId = in.readByte() == 0x00 ? null : in.readInt();
        quantity = in.readByte() == 0x00 ? null : in.readInt();
        productId = in.readByte() == 0x00 ? null : in.readInt();
        price = in.readByte() == 0x00 ? null : in.readInt();
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
        if (doId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(doId);
        }
        if (quantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(quantity);
        }
        if (productId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(productId);
        }
        if (price == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(price);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DeliveryOrderItem> CREATOR = new Parcelable.Creator<DeliveryOrderItem>() {
        @Override
        public DeliveryOrderItem createFromParcel(Parcel in) {
            return new DeliveryOrderItem(in);
        }

        @Override
        public DeliveryOrderItem[] newArray(int size) {
            return new DeliveryOrderItem[size];
        }
    };
}