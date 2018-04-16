package com.goleep.driverapp.viewmodels.pickup.returns;

import android.arch.lifecycle.ViewModel;

import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;

import java.util.List;

/**
 * Created by vishalm on 16/04/18.
 */
public class ReturnsInvoiceViewModel extends ViewModel {
    private Customer consumerLocation;
    private List<Product> scannedProducts;

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
}
