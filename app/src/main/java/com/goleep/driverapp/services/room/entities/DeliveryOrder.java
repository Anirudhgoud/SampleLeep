package com.goleep.driverapp.services.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.goleep.driverapp.helpers.uimodels.BaseListItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anurag on 16/02/18.
 */

@Entity
public class DeliveryOrder extends BaseListItem{

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("do_number")
    @Expose
    private String doNumber;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("source_location_id")
    @Expose
    private Integer sourceLocationId;
    @SerializedName("preferred_delivery_date")
    @Expose
    private String preferredDeliveryDate;
    @SerializedName("actual_delivery_date")
    @Expose
    private String actualDeliveryDate;
    @SerializedName("preferred_delivery_time")
    @Expose
    private String preferredDeliveryTime;
    @SerializedName("source_location_name")
    @Expose
    private String sourceLocationName;
    @SerializedName("destination_location_id")
    @Expose
    private Integer destinationLocationId;
    @SerializedName("destination_location_name")
    @Expose
    private String destinationLocationName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("assignee_name")
    @Expose
    private String assigneeName;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("destination_latitude")
    @Expose
    private Float destinationLatitude;
    @SerializedName("destination_longitude")
    @Expose
    private Float destinationLongitude;
    @SerializedName("destination_address_line_1")
    @Expose
    private String destinationAddressLine1;
    @SerializedName("destination_address_line_2")
    @Expose
    private String destinationAddressLine2;
    @SerializedName("delivery_order_items_count")
    @Expose
    private Integer deliveryOrderItemsCount;
    @SerializedName("total_value")
    @Expose
    private Float totalValue;

    @Ignore
    @SerializedName("duration_from_current_location")
    private Integer durationFromCurrentLocation = 0;


    public DeliveryOrder() {
        super(0);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getSourceLocationId() {
        return sourceLocationId;
    }

    public void setSourceLocationId(Integer sourceLocationId) {
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

    public Integer getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(Integer destinationLocationId) {
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

    public Float getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Float destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Float getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Float destinationLongitude) {
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

    public Integer getDeliveryOrderItemsCount() {
        return deliveryOrderItemsCount;
    }

    public void setDeliveryOrderItemsCount(Integer deliveryOrderItemsCount) {
        this.deliveryOrderItemsCount = deliveryOrderItemsCount;
    }

    public Float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Float totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getDurationFromCurrentLocation() {
        return durationFromCurrentLocation;
    }

    public void setDurationFromCurrentLocation(Integer durationFromCurrentLocation) {
        this.durationFromCurrentLocation = durationFromCurrentLocation;
    }

    public boolean isAllSelected(){
        return super.selectedCount == getDeliveryOrderItemsCount();
    }

}