package com.goleep.driverapp.services.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by vishalm on 22/03/18.
 */
@Entity
public class ReturnOrderEntity {
    @PrimaryKey
    private long roNumber;
    private String status;
    private String assigneeName;
    private String destinationLocationName;
    private String sourceLocationName;
    private String createdAt;
    private int returnOrderItemsCount;
    private double totalValue;
    private int sourceLocationId;
    private int destinationLocationId;
    private String customerName;
    private String sourceAddressLine1;
    private String sourceAddressLine2;
    private String actualReturnAt;

    public String getActualReturnAt() {
        return actualReturnAt;
    }

    public void setActualReturnAt(String actualReturnAt) {
        this.actualReturnAt = actualReturnAt;
    }

    public int getSourceLocationId() {
        return sourceLocationId;
    }

    public void setSourceLocationId(int sourceLocationId) {
        this.sourceLocationId = sourceLocationId;
    }

    public int getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(int destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSourceAddressLine1() {
        return sourceAddressLine1;
    }

    public void setSourceAddressLine1(String sourceAddressLine1) {
        this.sourceAddressLine1 = sourceAddressLine1;
    }

    public String getSourceAddressLine2() {
        return sourceAddressLine2;
    }

    public void setSourceAddressLine2(String sourceAddressLine2) {
        this.sourceAddressLine2 = sourceAddressLine2;
    }

    public long getRoNumber() {
        return roNumber;
    }

    public void setRoNumber(long roNUmber) {
        this.roNumber = roNUmber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getDestinationLocationName() {
        return destinationLocationName;
    }

    public void setDestinationLocationName(String destinationLocationName) {
        this.destinationLocationName = destinationLocationName;
    }

    public String getSourceLocationName() {
        return sourceLocationName;
    }

    public void setSourceLocationName(String sourceLocationName) {
        this.sourceLocationName = sourceLocationName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getReturnOrderItemsCount() {
        return returnOrderItemsCount;
    }

    public void setReturnOrderItemsCount(int returnOrderItemsCount) {
        this.returnOrderItemsCount = returnOrderItemsCount;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
}
