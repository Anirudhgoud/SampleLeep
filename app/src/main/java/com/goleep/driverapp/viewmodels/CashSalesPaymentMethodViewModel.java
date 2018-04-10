package com.goleep.driverapp.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;

import java.util.List;

public class CashSalesPaymentMethodViewModel extends ViewModel {

    private Customer consumerLocation;
    private List<Product> scannedProducts;
    private double previousBalance;
    private double paymentCollected;



    public double getTotalReturns(){
        return 0;
    }

    public double getCurrentSales(){
        double totalSales = 0;
        for (Product product : scannedProducts) {
            if (product == null) continue;
            totalSales += product.getTotalPrice();
        }
        return totalSales;
    }

    public double grandTotal(double returns, double currentSales, double previousBalance) {
        return currentSales + previousBalance - returns;
    }

    public double outstandingBalance(double grandTotal, double paymentCollected) {
        return grandTotal - paymentCollected;
    }

    //Getters and setters
    public Customer getConsumerLocation() {
        return consumerLocation;
    }

    public void setConsumerLocation(Customer consumerLocation) {
        this.consumerLocation = consumerLocation;
    }

    public List<Product> getScannedProducts() {
        return scannedProducts;
    }

    public void setScannedProducts(List<Product> scannedProducts) {
        this.scannedProducts = scannedProducts;
    }

    public double getPaymentCollected() {
        return paymentCollected;
    }

    public void setPaymentCollected(double paymentCollected) {
        this.paymentCollected = paymentCollected;
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
    }
}
