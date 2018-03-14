package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * Created by anurag on 13/03/18.
 */

public class DropOffPaymentConfirmationViewModel extends DropOffDoBaseViewModel {

    private String businessAddress;
    private double currentSale;
    private double outstandingBalance;
    private double paymentCollected;
    private double grandTotal;
    private double previousBalance;
    private String paymentMethod;

    public DropOffPaymentConfirmationViewModel(@NonNull Application application) {
        super(application);
    }

    //Getters and setters

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public double getCurrentSale() {
        return currentSale;
    }

    public void setCurrentSale(double currentSale) {
        this.currentSale = currentSale;
    }

    public double getOutstandingBalance() {
        return getGrandTotal() - paymentCollected;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public double getPaymentCollected() {
        return paymentCollected;
    }

    public void setPaymentCollected(double paymentCollected) {
        this.paymentCollected = paymentCollected;
    }

    public double getGrandTotal() {
        return currentSale + previousBalance;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
