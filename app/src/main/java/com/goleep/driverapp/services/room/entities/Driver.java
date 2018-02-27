package com.goleep.driverapp.services.room.entities;

/**
 * Created by vishalm on 15/02/18.
 */


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class Driver {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("licence_type")
    @Expose
    private String licenceType;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("experience")
    @Expose
    private String experience;
    @SerializedName("insurance")
    @Expose
    private Boolean insurance;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("licence_number")
    @Expose
    private String licenceNumber;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("licence_expiration_date")
    @Expose
    private String licenceExpirationDate;
//    @SerializedName("last_sign_in_at")
//    @Expose
//    private Object lastSignInAt;
    @SerializedName("completed_delivery_orders_count")
    @Expose
    private Integer completedDeliveryOrdersCount;
    @SerializedName("payment_collected")
    @Expose
    private Integer paymentCollected;
    @SerializedName("delivery_locations_count")
    @Expose
    private Integer deliveryLocationsCount;
    @SerializedName("address_line_1")
    @Expose
    private String addressLine1;
    @SerializedName("address_line_2")
    @Expose
    private String addressLine2;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("pin_code")
    @Expose
    private String pinCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenceType() {
        return licenceType;
    }

    public void setLicenceType(String licenceType) {
        this.licenceType = licenceType;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
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

    public String getLicenceExpirationDate() {
        return licenceExpirationDate;
    }

    public void setLicenceExpirationDate(String licenceExpirationDate) {
        this.licenceExpirationDate = licenceExpirationDate;
    }

//    public Object getLastSignInAt() {
//        return lastSignInAt;
//    }
//
//    public void setLastSignInAt(Object lastSignInAt) {
//        this.lastSignInAt = lastSignInAt;
//    }

    public Integer getCompletedDeliveryOrdersCount() {
        return completedDeliveryOrdersCount;
    }

    public void setCompletedDeliveryOrdersCount(Integer completedDeliveryOrdersCount) {
        this.completedDeliveryOrdersCount = completedDeliveryOrdersCount;
    }

    public Integer getPaymentCollected() {
        return paymentCollected;
    }

    public void setPaymentCollected(Integer paymentCollected) {
        this.paymentCollected = paymentCollected;
    }

    public Integer getDeliveryLocationsCount() {
        return deliveryLocationsCount;
    }

    public void setDeliveryLocationsCount(Integer deliveryLocationsCount) {
        this.deliveryLocationsCount = deliveryLocationsCount;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
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

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

}