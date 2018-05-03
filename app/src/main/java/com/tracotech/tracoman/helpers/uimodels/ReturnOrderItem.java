package com.tracotech.tracoman.helpers.uimodels;

import com.tracotech.tracoman.services.room.entities.OrderItemEntity;

/**
 * Created by vishalm on 23/03/18.
 */

public class ReturnOrderItem extends OrderItemEntity {
    private String reason;
    private long roId;

    public long getRoId() {
        return roId;
    }

    public void setRoId(long roId) {
        this.roId = roId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
