package com.tracotech.tracoman.viewmodels.dropoff.deliveryorders;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.services.room.AppDatabase;
import com.tracotech.tracoman.services.room.RoomDBService;
import com.tracotech.tracoman.services.room.entities.DeliveryOrderEntity;
import com.tracotech.tracoman.services.room.entities.OrderItemEntity;

import java.util.List;

/**
 * Created by anurag on 13/03/18.
 */

public class DropOffDoBaseViewModel extends AndroidViewModel {

    protected AppDatabase leepDatabase;

    protected int deliveryOrderId;
    protected DeliveryOrderEntity deliveryOrder;
    protected List<OrderItemEntity> orderItems;

    public DropOffDoBaseViewModel(@NonNull Application application) {
        super(application);
        leepDatabase = RoomDBService.sharedInstance().getDatabase(application);
    }

    public DeliveryOrderEntity deliveryOrder(int deliveryOrderId) {
        return leepDatabase.deliveryOrderDao().deliveryOrder(deliveryOrderId);
    }

    public List<OrderItemEntity> getSelectedOrderItems() {
        return leepDatabase.deliveryOrderItemDao().getSelectedOrderItems(deliveryOrderId);
    }

    //Getters and setters

    public int getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(int deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public DeliveryOrderEntity getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrderEntity deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }
}
