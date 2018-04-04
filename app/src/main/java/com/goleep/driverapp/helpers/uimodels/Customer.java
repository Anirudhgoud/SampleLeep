package com.goleep.driverapp.helpers.uimodels;

/**
 * Created by anurag on 20/03/18.
 */

public class Customer {
    private int id;
    private String name;
    private int businessId;
    private String area;
    private String lastDeliveryDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLastDeliveryDate() {
        return lastDeliveryDate;
    }

    public void setLastDeliveryDate(String lastDeliveryDate) {
        this.lastDeliveryDate = lastDeliveryDate;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }
}
