package com.goleep.driverapp.services.room.entities;

/**
 * Created by vishalm on 15/02/18.
 */


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class DriverEntity {
    @PrimaryKey
    private int id;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String email;
    private String licenceType;
    private int age;
    private String experience;
    private boolean insurance;
    private String dateOfBirth;
    private String contactNumber;
    private int locationId;
    private String licenceNumber;
    private String vehicleNumber;
    private String licenceExpirationDate;
    private int completedDeliveryOrdersCount;
    private int paymentCollected;
    private int deliveryLocationsCount;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private int countryId;
    private String countryName;
    private String pinCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
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

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
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

    public int getCompletedDeliveryOrdersCount() {
        return completedDeliveryOrdersCount;
    }

    public void setCompletedDeliveryOrdersCount(int completedDeliveryOrdersCount) {
        this.completedDeliveryOrdersCount = completedDeliveryOrdersCount;
    }

    public int getPaymentCollected() {
        return paymentCollected;
    }

    public void setPaymentCollected(int paymentCollected) {
        this.paymentCollected = paymentCollected;
    }

    public int getDeliveryLocationsCount() {
        return deliveryLocationsCount;
    }

    public void setDeliveryLocationsCount(int deliveryLocationsCount) {
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

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
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