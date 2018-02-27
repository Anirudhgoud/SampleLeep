package com.goleep.driverapp.services.room.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.v4.widget.DrawerLayout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vishalm on 09/02/18.
 */

@Entity
public class UserMeta {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private Integer id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("business_id")
    @Expose
    private Integer businessId;
    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;
    @SerializedName("locations")
    @Expose
    private List<Location> locations = null;
    @SerializedName("permissions")
    @Expose
    private Permissions permissions;
    @SerializedName("driver")
    @Expose
    private Driver driver;

    public UserMeta(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public class Permissions {

        @SerializedName("web_login")
        @Expose
        private Boolean webLogin;
        @SerializedName("mobile_login")
        @Expose
        private Boolean mobileLogin;
        @SerializedName("customers")
        @Expose
        private Boolean customers;
        @SerializedName("products")
        @Expose
        private Boolean products;
        @SerializedName("drivers")
        @Expose
        private Boolean drivers;
        @SerializedName("stocks")
        @Expose
        private Boolean stocks;
        @SerializedName("delivery_orders")
        @Expose
        private Boolean deliveryOrders;
        @SerializedName("return_orders")
        @Expose
        private Boolean returnOrders;
        @SerializedName("settings")
        @Expose
        private Boolean settings;

        public Permissions(){

        }

        public Boolean getWebLogin() {
            return webLogin;
        }

        public void setWebLogin(Boolean webLogin) {
            this.webLogin = webLogin;
        }

        public Boolean getMobileLogin() {
            return mobileLogin;
        }

        public void setMobileLogin(Boolean mobileLogin) {
            this.mobileLogin = mobileLogin;
        }

        public Boolean getCustomers() {
            return customers;
        }

        public void setCustomers(Boolean customers) {
            this.customers = customers;
        }

        public Boolean getProducts() {
            return products;
        }

        public void setProducts(Boolean products) {
            this.products = products;
        }

        public Boolean getDrivers() {
            return drivers;
        }

        public void setDrivers(Boolean drivers) {
            this.drivers = drivers;
        }

        public Boolean getStocks() {
            return stocks;
        }

        public void setStocks(Boolean stocks) {
            this.stocks = stocks;
        }

        public Boolean getDeliveryOrders() {
            return deliveryOrders;
        }

        public void setDeliveryOrders(Boolean deliveryOrders) {
            this.deliveryOrders = deliveryOrders;
        }

        public Boolean getReturnOrders() {
            return returnOrders;
        }

        public void setReturnOrders(Boolean returnOrders) {
            this.returnOrders = returnOrders;
        }

        public Boolean getSettings() {
            return settings;
        }

        public void setSettings(Boolean settings) {
            this.settings = settings;
        }

    }

    public class Location {

        @SerializedName("id")
        @Expose
        private Integer locationId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("address_line_1")
        @Expose
        private String addressLine1;
        @SerializedName("address_line_2")
        @Expose
        private String addressLine2;
        @SerializedName("redistribution_centre")
        @Expose
        private String redistributionCentre;

        public Integer getLocationId() {
            return locationId;
        }

        public void setLocationId(Integer locationId) {
            this.locationId = locationId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getRedistributionCentre() {
            return redistributionCentre;
        }

        public void setRedistributionCentre(String redistributionCentre) {
            this.redistributionCentre = redistributionCentre;
        }

    }

    public class Driver {

        @SerializedName("id")
        @Expose
        private Integer driverId;
        @SerializedName("licence_number")
        @Expose
        private String licenceNumber;
        @SerializedName("vehicle_number")
        @Expose
        private String vehicleNumber;
        @SerializedName("experience")
        @Expose
        private String experience;
        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("longitude")
        @Expose
        private Double longitude;
        @SerializedName("date_of_birth")
        @Expose
        private String dateOfBirth;

        public Driver(){

        }

        public Integer getDriverId() {
            return driverId;
        }

        public void setDriverId(Integer driverId) {
            this.driverId = driverId;
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

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

    }



}