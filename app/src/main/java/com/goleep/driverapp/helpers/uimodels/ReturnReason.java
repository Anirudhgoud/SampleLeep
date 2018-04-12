package com.goleep.driverapp.helpers.uimodels;

/**
 * Created by vishalm on 05/04/18.
 */

public class ReturnReason {

    private int id;
    private String reason;
    private boolean isSelected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
