package com.goleep.driverapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;

public class CashSalesConfirmationViewModel extends AndroidViewModel {

    private Customer consumerLocation;
    private ArrayList<Product> scannedProducts;

    public CashSalesConfirmationViewModel(@NonNull Application application) {
        super(application);
    }


    public String currentDateToDisplay() {
        Date now = new Date();
        return DateTimeUtils.convertedDate(now, DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT_COMMA);
    }

    public String currentTimeToDisplay() {
        Date now = new Date();
        return DateTimeUtils.convertedDate(now, DateTimeUtils.TWELVE_HOUR_TIME_FORMAT);
    }

    //Getters and setters
    public Customer getConsumerLocation() {
        return consumerLocation;
    }

    public void setConsumerLocation(Customer consumerLocation) {
        this.consumerLocation = consumerLocation;
    }

    public ArrayList<Product> getScannedProducts() {
        return scannedProducts == null ? new ArrayList<>() : scannedProducts;
    }

    public void setScannedProducts(ArrayList<Product> scannedProducts) {
        this.scannedProducts = scannedProducts;
    }
}
