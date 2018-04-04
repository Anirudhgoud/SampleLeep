package com.goleep.driverapp.helpers.uimodels;

/**
 * Created by shubham on 04/04/2018.
 */

public class GetBusinessesData {
    private  int id;
    private String name;
    private String imageUrl;
    private String category;
    private int locationsCount;
    private boolean active;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public int getLocationsCount() {
        return locationsCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLocationsCount(int locationsCount) {
        this.locationsCount = locationsCount;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
