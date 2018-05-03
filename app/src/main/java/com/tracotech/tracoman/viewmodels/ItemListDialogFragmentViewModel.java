package com.tracotech.tracoman.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.services.room.RoomDBService;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;

import java.util.List;

/**
 * Created by anurag on 13/03/18.
 */

public class ItemListDialogFragmentViewModel extends AndroidViewModel {

    private List<OrderItemEntity> orderItems;
    private int deliveryOrderId;
    private double grandTotal;

    public ItemListDialogFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchOrderItems() {
        orderItems = RoomDBService.sharedInstance().getDatabase(getApplication().getApplicationContext()).deliveryOrderItemDao().getSelectedOrderItems(deliveryOrderId);
    }

    //Getters and setters
    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public int getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(int deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }
}
