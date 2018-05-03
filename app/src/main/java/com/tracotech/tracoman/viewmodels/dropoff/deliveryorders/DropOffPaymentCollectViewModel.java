package com.tracotech.tracoman.viewmodels.dropoff.deliveryorders;

import android.app.Application;
import android.support.annotation.NonNull;

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
