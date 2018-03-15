package com.goleep.driverapp.interfaces;

import com.goleep.driverapp.services.room.entities.OrderItemEntity;

import java.util.List;

/**
 * Created by vishalm on 07/03/18.
 */

public interface OrderItemsChangeListener {
    void itemsChanged(List<OrderItemEntity> orderItems);
}
