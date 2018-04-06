package com.goleep.driverapp.leep;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;

import java.util.ArrayList;

public class CashSalesConfirmationViewModel extends AndroidViewModel {

    private Customer consumerLocation;
    private ArrayList<Product> scannedProducts;

    public CashSalesConfirmationViewModel(@NonNull Application application) {
        super(application);
    }



    //Getters and setters
    public Customer getConsumerLocation() {
        return consumerLocation;
    }

    public void setConsumerLocation(Customer consumerLocation) {
        this.consumerLocation = consumerLocation;
    }

    public ArrayList<Product> getScannedProducts() {
        return scannedProducts;
    }

    public void setScannedProducts(ArrayList<Product> scannedProducts) {
        this.scannedProducts = scannedProducts;
    }
}
