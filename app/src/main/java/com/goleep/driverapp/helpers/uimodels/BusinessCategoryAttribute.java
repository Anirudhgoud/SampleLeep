package com.goleep.driverapp.helpers.uimodels;

/**
 * Created by shubham on 03/04/2018.
 */

public class BusinessCategoryAttribute {
    int id;
    String name;
    int businessesCount;
    public int getId() {
        return id;
    }

    public String getBusinessName() {
        return name;
    }

    public int getBusinessesCount() {
        return businessesCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBusinessesCount(int businessesCount) {
        this.businessesCount = businessesCount;
    }
}
