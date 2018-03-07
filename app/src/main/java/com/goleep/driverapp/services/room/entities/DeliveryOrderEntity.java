package com.goleep.driverapp.services.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;

import javax.annotation.Nullable;

/**
 * Created by anurag on 16/02/18.
 */

@Entity
public class DeliveryOrderEntity extends BaseListItem{

    @PrimaryKey
    private int id;
    private String doNumber;
    private String status;
    private String type;
    private int sourceLocationId;
    private String preferredDeliveryDate;
    private String actualDeliveryDate;
    private String preferredDeliveryTime;
    private String sourceLocationName;
    private int destinationLocationId;
    private String destinationLocationName;
    private String createdAt;
    private String assigneeName;
    private String customerName;
    private long destinationLatitude;
    private long destinationLongitude;
    private String destinationAddressLine1;
    private String destinationAddressLine2;
    private int deliveryOrderItemsCount;
    private float totalValue;

    @Ignore
    private int durationFromCurrentLocation = 0;



    public DeliveryOrderEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoNumber() {
        return doNumber;
    }

    public void setDoNumber(String doNumber) {
        this.doNumber = doNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSourceLocationId() {
        return sourceLocationId;
    }

    public void setSourceLocationId(int sourceLocationId) {
        this.sourceLocationId = sourceLocationId;
    }

    public String getPreferredDeliveryDate() {
        return preferredDeliveryDate;
    }

    public void setPreferredDeliveryDate(String preferredDeliveryDate) {
        this.preferredDeliveryDate = preferredDeliveryDate;
    }

    public String getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(String actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public String getPreferredDeliveryTime() {
        return preferredDeliveryTime;
    }

    public void setPreferredDeliveryTime(String preferredDeliveryTime) {
        this.preferredDeliveryTime = preferredDeliveryTime;
    }

    public String getSourceLocationName() {
        return sourceLocationName;
    }

    public void setSourceLocationName(String sourceLocationName) {
        this.sourceLocationName = sourceLocationName;
    }

    public int getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(int destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
    }

    public String getDestinationLocationName() {
        return destinationLocationName;
    }

    public void setDestinationLocationName(String destinationLocationName) {
        this.destinationLocationName = destinationLocationName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(long destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public long getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(long destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public String getDestinationAddressLine1() {
        return destinationAddressLine1;
    }

    public void setDestinationAddressLine1(String destinationAddressLine1) {
        this.destinationAddressLine1 = destinationAddressLine1;
    }

    public String getDestinationAddressLine2() {
        return destinationAddressLine2;
    }

    public void setDestinationAddressLine2(String destinationAddressLine2) {
        this.destinationAddressLine2 = destinationAddressLine2;
    }

    public int getDeliveryOrderItemsCount() {
        return deliveryOrderItemsCount;
    }

    public void setDeliveryOrderItemsCount(int deliveryOrderItemsCount) {
        this.deliveryOrderItemsCount = deliveryOrderItemsCount;
    }

    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }

    public int getDurationFromCurrentLocation() {
        return durationFromCurrentLocation;
    }

    public void setDurationFromCurrentLocation(int durationFromCurrentLocation) {
        this.durationFromCurrentLocation = durationFromCurrentLocation;
    }
}