package com.goleep.driverapp.helpers.uimodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vishalm on 09/02/18.
 */


public class UserMeta {

    @SerializedName("id")
    @Expose
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

    private class Permissions {

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

    private class Location {

        @SerializedName("id")
        @Expose
        private Integer id;
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

}