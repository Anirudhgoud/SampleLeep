package com.goleep.driverapp.services.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vishalm on 22/02/18.
 */
@Entity
public class DoDetails {

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("delivery_confirmation_type")
    @Expose
    private String deliveryConfirmationType;
    @SerializedName("do_number")
    @Expose
    private Integer doNumber;
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("received_by")
    @Expose
    private String receivedBy;
    @SerializedName("received_at")
    @Expose
    private String receivedAt;
    @SerializedName("preferred_delivery_date")
    @Expose
    private String preferredDeliveryDate;
    @SerializedName("actual_delivery_date")
    @Expose
    private String actualDeliveryDate;
    @SerializedName("preferred_delivery_time")
    @Expose
    private String preferredDeliveryTime;
    @SerializedName("delivery_order_items")
    @Expose
    private List<DeliveryOrderItem> deliveryOrderItems = null;
    @SerializedName("assignee")
    @Expose
    private Assignee assignee;
    @SerializedName("source_location")
    @Expose
    private SourceLocation sourceLocation;
    @SerializedName("destination_location")
    @Expose
    private DestinationLocation destinationLocation;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveryConfirmationType() {
        return deliveryConfirmationType;
    }

    public void setDeliveryConfirmationType(String deliveryConfirmationType) {
        this.deliveryConfirmationType = deliveryConfirmationType;
    }

    public Integer getDoNumber() {
        return doNumber;
    }

    public void setDoNumber(Integer doNumber) {
        this.doNumber = doNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(String receivedAt) {
        this.receivedAt = receivedAt;
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

    public List<DeliveryOrderItem> getDeliveryOrderItems() {
        return deliveryOrderItems;
    }

    public void setDeliveryOrderItems(List<DeliveryOrderItem> deliveryOrderItems) {
        this.deliveryOrderItems = deliveryOrderItems;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public DestinationLocation getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(DestinationLocation destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public class Assignee {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("phone_no")
        @Expose
        private String phoneNo;
        @SerializedName("coordinates")
        @Expose
        private List<Double> coordinates = null;
        @SerializedName("licence_number")
        @Expose
        private String licenceNumber;
        @SerializedName("vehicle_number")
        @Expose
        private String vehicleNumber;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }

        public String getLicenceNumber() {
            return licenceNumber;
        }

        public void setLicenceNumber(String licenceNumber) {
            this.licenceNumber = licenceNumber;
        }

        public String getVehicleNumber() {
            return vehicleNumber;
        }

        public void setVehicleNumber(String vehicleNumber) {
            this.vehicleNumber = vehicleNumber;
        }

    }



    public class DestinationLocation {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("coordinates")
        @Expose
        private List<Object> coordinates = null;
        @SerializedName("business_name")
        @Expose
        private String businessName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Object> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Object> coordinates) {
            this.coordinates = coordinates;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

    }




    public class SourceLocation {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("coordinates")
        @Expose
        private List<Object> coordinates = null;
        @SerializedName("business_name")
        @Expose
        private String businessName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Object> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Object> coordinates) {
            this.coordinates = coordinates;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

    }
}