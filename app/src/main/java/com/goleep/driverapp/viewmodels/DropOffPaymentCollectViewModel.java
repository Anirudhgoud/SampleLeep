package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.support.annotation.NonNull;

import com.goleep.driverapp.helpers.uimodels.Location;
import com.goleep.driverapp.utils.DateTimeUtils;

/**
 * Created by anurag on 09/03/18.
 */

public class DropOffPaymentCollectViewModel extends DropOffDoBaseViewModel {
    private double outstandingBalance;
    private double currentSales;
    private String businessAddress;

    public DropOffPaymentCollectViewModel(@NonNull Application application) {
        super(application);
    }

    public String dateToDisplay(String dateString) {
        return (dateString == null) ? "-" : DateTimeUtils.convertdDate(dateString,
                DateTimeUtils.ORDER_SERVER_DATE_FORMAT, DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA);
    }

    public String getAddress(Location location) {
        String address = "";
        if (location == null) {
            return address;
        }
        address = location.getAddressLine1() + ",\n" + location.getAddressLine2() + ",\n" + location.getCity() + ", " + location.getState() + " " + location.getPincode();
        return address;
    }

    //Getters and Setters
    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public double getCurrentSales() {
        return currentSales;
    }

    public void setCurrentSales(double currentSales) {
        this.currentSales = currentSales;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }
}
