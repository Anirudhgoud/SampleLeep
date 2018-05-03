package com.tracotech.tracoman.services.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.tracotech.tracoman.helpers.uimodels.BaseListItem;
import com.tracotech.tracoman.helpers.uimodels.Distance;

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
    private double destinationLatitude;
    private double destinationLongitude;
    private double sourceLatitude;
    private double sourceLongitude;
    private String city;
    private String state;
    private String pincode;
    private String sourceAddressLine1;
    private String sourceAddressLine2;
    private String destinationAddressLine1;
    private String destinationAddressLine2;
    private int deliveryOrderItemsCount;
    private float totalValue;

    @Ignore
    private Distance distanceFromCurrentLocation;


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

    public Distance getDistanceFromCurrentLocation() {
        return distanceFromCurrentLocation;
    }

    public void setDistanceFromCurrentLocation(Distance distanceFromCurrentLocation) {
        this.distanceFromCurrentLocation = distanceFromCurrentLocation;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
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

    public double getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(double sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public double getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(double sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}