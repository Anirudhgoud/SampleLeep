package com.goleep.driverapp.services.network.responsemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vishalm on 01/03/18.
 */

public class DoDetailResponseModel {
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("delivery_confirmation_type")
    @Expose
    private Object deliveryConfirmationType;
    @SerializedName("do_number")
    @Expose
    private Integer doNumber;
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
    private Object receivedBy;
    @SerializedName("received_at")
    @Expose
    private Object receivedAt;
    @SerializedName("preferred_delivery_date")
    @Expose
    private String preferredDeliveryDate;
    @SerializedName("actual_delivery_date")
    @Expose
    private Object actualDeliveryDate;
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

    public Object getDeliveryConfirmationType() {
        return deliveryConfirmationType;
    }

    public void setDeliveryConfirmationType(Object deliveryConfirmationType) {
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

    public Object getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(Object receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Object getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Object receivedAt) {
        this.receivedAt = receivedAt;
    }

    public String getPreferredDeliveryDate() {
        return preferredDeliveryDate;
    }

    public void setPreferredDeliveryDate(String preferredDeliveryDate) {
        this.preferredDeliveryDate = preferredDeliveryDate;
    }

    public Object getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(Object actualDeliveryDate) {
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

    private class Assignee {

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


    public class DeliveryOrderItem {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("quantity")
        @Expose
        private Integer quantity;
        @SerializedName("product")
        @Expose
        private Product product;
        @SerializedName("price")
        @Expose
        private Integer price;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

    }

    public class Product {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("weight")
        @Expose
        private String weight;
        @SerializedName("weight_unit")
        @Expose
        private String weightUnit;

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

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getWeightUnit() {
            return weightUnit;
        }

        public void setWeightUnit(String weightUnit) {
            this.weightUnit = weightUnit;
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

    private class DestinationLocation {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("contact_name_1")
        @Expose
        private String contactName1;
        @SerializedName("contact_phone_1")
        @Expose
        private String contactPhone1;
        @SerializedName("contact_email_1")
        @Expose
        private String contactEmail1;
        @SerializedName("coordinates")
        @Expose
        private List<Object> coordinates = null;
        @SerializedName("business_name")
        @Expose
        private String businessName;
        @SerializedName("business_type")
        @Expose
        private String businessType;

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

        public String getContactName1() {
            return contactName1;
        }

        public void setContactName1(String contactName1) {
            this.contactName1 = contactName1;
        }

        public String getContactPhone1() {
            return contactPhone1;
        }

        public void setContactPhone1(String contactPhone1) {
            this.contactPhone1 = contactPhone1;
        }

        public String getContactEmail1() {
            return contactEmail1;
        }

        public void setContactEmail1(String contactEmail1) {
            this.contactEmail1 = contactEmail1;
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

        public String getBusinessType() {
            return businessType;
        }

        public void setBusinessType(String businessType) {
            this.businessType = businessType;
        }

    }

}
