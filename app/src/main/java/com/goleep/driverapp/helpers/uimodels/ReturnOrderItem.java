package com.goleep.driverapp.helpers.uimodels;

import com.goleep.driverapp.services.room.entities.OrderItemEntity;

/**
 * Created by vishalm on 23/03/18.
 */

public class ReturnOrderItem extends OrderItemEntity {
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String reason;

}
