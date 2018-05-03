package com.tracotech.tracoman.viewmodels.pickup.returns;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.tracotech.tracoman.helpers.uimodels.Customer;
import com.tracotech.tracoman.helpers.uimodels.Product;

import java.util.List;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsPaymentMethodViewModel extends AndroidViewModel{
    protected Customer consumerLocation;
    protected List<Product> scannedProducts;
    protected double previousBalance;
    protected double paymentCollected;

    public ReturnsPaymentMethodViewModel(@NonNull Application application) {
        super(application);
    }

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

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
    }

    public double getPaymentCollected() {
        return paymentCollected;
    }

    public void setPaymentCollected(double paymentCollected) {
        this.paymentCollected = paymentCollected;
    }


    public double getTotalReturns() {
        double totalReturns = 0;
        for (Product product : scannedProducts) {
            if (product == null) continue;
            totalReturns += product.getTotalReturnsPrice();
        }
        return totalReturns;
    }

    public double grandTotal(double returns, double previousBalance) {
        return previousBalance - returns;
    }

    public double outstandingBalance(double grandTotal, double paymentCollected) {
        return grandTotal - paymentCollected;
    }
}


